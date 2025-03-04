#!/usr/bin/env bash

set -e

include "_env_variables_source_me.sh"


JENKINS_CRUMB_HEADER=""
JENKINS_CRUMB=""

# Function to get CSRF crumb for Jenkins API requests
get_jenkins_crumb() {
    # Get CSRF crumb
    echo -e "${YELLOW}Getting Jenkins crumb...${NC}"
    local crumb_response
    crumb_response=$(curl -s -u "${ADMIN_USER:?}:${ADMIN_PASSWORD:?}" "${JENKINS_URL:?}/crumbIssuer/api/json")

    if [[ "${crumb_response}" == *"crumb"* ]]; then
        JENKINS_CRUMB=$(echo "${crumb_response}" | grep -o '"crumb":"[^"]*' | cut -d'"' -f4)
        JENKINS_CRUMB_HEADER=$(echo "${crumb_response}" | grep -o '"crumbRequestField":"[^"]*' | cut -d'"' -f4)
        echo -e "${GREEN}Successfully obtained Jenkins crumb.${NC}"
    else
        echo -e "${YELLOW}Could not get crumb. Continuing without it...${NC}"
    fi
}

# Function to check if Jenkins is running
is_jenkins_running() {
    local status_code
    status_code=$(curl -s -o /dev/null -w "%{http_code}" "${JENKINS_URL:?}/")
    if [[ "${status_code}" == "200" || "${status_code}" == "403" ]]; then
        return 0
    else
        return 1
    fi
}

# Function to create API token
create_api_token() {
    echo -e "${YELLOW}Creating Jenkins API token...${NC}"
    local token_response

    # Form the curl command with or without crumb header
    local curl_cmd="curl -s -u \"${ADMIN_USER:?}:${ADMIN_PASSWORD:?}\" -X POST"

    if [[ -n "${JENKINS_CRUMB}" && -n "${JENKINS_CRUMB_HEADER}" ]]; then
        curl_cmd="${curl_cmd} -H \"${JENKINS_CRUMB_HEADER}: ${JENKINS_CRUMB}\""
    fi

    # Complete the curl command
    curl_cmd="${curl_cmd} \"${JENKINS_URL:?}/user/${ADMIN_USER:?}/descriptorByName/jenkins.security.ApiTokenProperty/generateNewToken\" --data 'newTokenName=cli-token'"

    # Execute the command
    token_response=$(eval "${curl_cmd}")

    if [[ "${token_response}" == *"tokenValue"* ]]; then
        local api_token
        api_token=$(echo "${token_response}" | grep -o '"tokenValue":"[^"]*' | cut -d'"' -f4)
        echo "${api_token}"
    else
        echo -e "${RED}Failed to create API token. Response: ${token_response}${NC}"
        return 1
    fi
}

# Download Jenkins CLI
download_jenkins_cli() {
    if [ ! -f "${JENKINS_CLI_JAR:?}" ]; then
        echo -e "${YELLOW}Downloading Jenkins CLI...${NC}"
        curl -s -o "${JENKINS_CLI_JAR:?}" "${JENKINS_URL:?}/jnlpJars/jenkins-cli.jar"

        if [ ! -f "${JENKINS_CLI_JAR:?}" ]; then
            throw "Failed to download Jenkins CLI jar from ${JENKINS_URL:?}/jnlpJars/jenkins-cli.jar"
        fi
    fi
}

# Wait for Jenkins to be fully up and running
wait_for_jenkins() {
    echo -e "${YELLOW}Waiting for Jenkins to start...${NC}"
    local attempts=0
    local max_attempts=30

    while [ $attempts -lt $max_attempts ]; do
        if is_jenkins_running; then
            echo -e "${GREEN}Jenkins is up and running!${NC}"
            sleep 5  # Give it a little more time to fully initialize
            return 0
        fi

        attempts=$((attempts+1))
        echo -e "${YELLOW}Waiting for Jenkins to start... (Attempt $attempts/$max_attempts)${NC}"
        sleep 10
    done

    throw "Jenkins did not start within the expected time."
}

# Check for plugins directory - create initial security file if it doesn't exist
ensure_security_config() {
    local config_xml="${JENKINS_HOME:?}/config.xml"

    # If config.xml doesn't exist, Jenkins might not be fully initialized
    if [ ! -f "${config_xml}" ]; then
        echo -e "${YELLOW}Jenkins config.xml not found. Waiting for full initialization...${NC}"
        return 1
    fi

    # Check if security is already disabled for CLI
    if grep -q "hudson.security.csrf.DefaultCrumbIssuer" "${config_xml}"; then
        if grep -q "<excludeClientIPFromCrumb>true</excludeClientIPFromCrumb>" "${config_xml}"; then
            echo -e "${GREEN}CSRF protection already configured correctly.${NC}"
            return 0
        fi

        # Update the configuration to exclude client IP from crumb check
        echo -e "${YELLOW}Updating CSRF protection settings...${NC}"
        sed -i.bak 's/<hudson.security.csrf.DefaultCrumbIssuer>/<hudson.security.csrf.DefaultCrumbIssuer>\n    <excludeClientIPFromCrumb>true<\/excludeClientIPFromCrumb>/g' "${config_xml}"

        # Restart Jenkins to apply changes
        ./stop-jenkins.sh
        sleep 5
        ./start-jenkins.sh
        wait_for_jenkins
        return 0
    fi

    return 0
}

# Install plugins
install_plugins() {
    echo -e "${YELLOW}Installing Jenkins plugins...${NC}"

    # Check if plugins.txt exists
    if [ ! -f "plugins.txt" ]; then
        throw "plugins.txt not found"
    fi

    # Download Jenkins CLI
    download_jenkins_cli

    # Get Jenkins crumb
    get_jenkins_crumb

    # Create API token for authentication
    API_TOKEN=$(create_api_token)

    if [ -z "${API_TOKEN}" ]; then
        echo -e "${YELLOW}Failed to create API token. Falling back to password authentication...${NC}"
        API_TOKEN="${ADMIN_PASSWORD:?}"
    else
        echo -e "${GREEN}Successfully created API token.${NC}"
    fi

    # Install each plugin
    while read -r plugin; do
        if [ -n "${plugin}" ] && [[ ! "${plugin}" =~ ^# ]]; then
            echo -e "${YELLOW}Installing plugin: ${plugin}${NC}"

            # Construct the CLI command for installing plugin
            local cli_cmd="java -jar \"${JENKINS_CLI_JAR:?}\" -s \"${JENKINS_URL:?}\" -auth \"${ADMIN_USER:?}:${API_TOKEN}\""

            if [[ -n "${JENKINS_CRUMB}" && -n "${JENKINS_CRUMB_HEADER}" ]]; then
                cli_cmd="${cli_cmd} -crumb \"${JENKINS_CRUMB}\""
            fi

            cli_cmd="${cli_cmd} install-plugin \"${plugin}\" -deploy"

            # Execute the command
            if ! eval "${cli_cmd}"; then
                echo -e "${RED}Failed to install plugin: ${plugin}. Continuing with others...${NC}"
            fi
        fi
    done < plugins.txt

    echo -e "${GREEN}All plugins installed!${NC}"

    # Restart Jenkins to apply plugin changes
    echo -e "${YELLOW}Restarting Jenkins to apply plugin changes...${NC}"

    # Construct the CLI command for restarting
    local restart_cmd="java -jar \"${JENKINS_CLI_JAR:?}\" -s \"${JENKINS_URL:?}\" -auth \"${ADMIN_USER:?}:${API_TOKEN}\""

    if [[ -n "${JENKINS_CRUMB}" && -n "${JENKINS_CRUMB_HEADER}" ]]; then
        restart_cmd="${restart_cmd} -crumb \"${JENKINS_CRUMB}\""
    fi

    restart_cmd="${restart_cmd} safe-restart"

    if ! eval "${restart_cmd}"; then
        echo -e "${YELLOW}Failed to restart Jenkins via CLI. Trying to restart manually...${NC}"
        ./stop-jenkins.sh
        sleep 5
        ./start-jenkins.sh
    fi

    # Wait for Jenkins to come back up
    sleep 10
    wait_for_jenkins
}

# Main execution
if is_jenkins_running; then
    ensure_security_config && install_plugins
else
    throw "Jenkins is not running. Please start Jenkins first."
fi
