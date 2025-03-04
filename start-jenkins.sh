#!/usr/bin/env bash

set -e

include "_env_variables_source_me.sh"

# Check if Jenkins is installed
if ! brew list --formula | grep -q jenkins-lts; then
    echo -e "${RED}Error: Jenkins is not installed.${NC}"
    echo -e "${YELLOW}Please run ./install-jenkins.sh first.${NC}"
    exit 1
fi

# Check if Jenkins is already running
if pgrep -f "jenkins.war" > /dev/null; then
    echo -e "${YELLOW}Jenkins is already running.${NC}"
    echo -e "${GREEN}Jenkins is available at http://localhost:8080/${NC}"
    exit 0
fi

# Verify SSH key
SSH_KEY_PATH=${SSH_KEY_PATH:-~/.ssh/id_rsa}
if [ ! -f "$SSH_KEY_PATH" ]; then
    echo -e "${RED}Error: SSH key not found at $SSH_KEY_PATH${NC}"
    echo -e "${YELLOW}Please provide a valid SSH key path with SSH_KEY_PATH environment variable${NC}"
    exit 1
fi

# Copy SSH key to Jenkins home if needed
if [ ! -f "$JENKINS_HOME/.ssh/id_rsa" ]; then
    echo -e "${YELLOW}Copying SSH key to Jenkins home...${NC}"
    mkdir -p "$JENKINS_HOME/.ssh"
    cp "$SSH_KEY_PATH" "$JENKINS_HOME/.ssh/id_rsa"
    chmod 600 "$JENKINS_HOME/.ssh/id_rsa"
    ssh-keyscan gitlab.com >> "$JENKINS_HOME/.ssh/known_hosts"
fi

# Load environment variables
if [ -f "$JENKINS_ENVIRONMENT" ]; then
    echo -e "${YELLOW}Loading Jenkins environment variables...${NC}"
    source "$JENKINS_ENVIRONMENT"
else
    echo -e "${YELLOW}Creating default environment variables...${NC}"
    cat > "$JENKINS_ENVIRONMENT" << EOF
JENKINS_HOME=$JENKINS_HOME
CASC_JENKINS_CONFIG=$JENKINS_HOME/casc_configs/jenkins.yaml
JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
THORG_ROOT=$JENKINS_HOME/workspace/thorg-root
EOF
    source "$JENKINS_ENVIRONMENT"
fi

# Start Jenkins
echo -e "${YELLOW}Starting Jenkins...${NC}"
brew services start jenkins-lts

# Function to check if Jenkins is running
is_jenkins_running() {
    curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/ | grep -q "200\|403"
    return $?
}

# Wait for Jenkins to start
echo -e "${YELLOW}Waiting for Jenkins to start...${NC}"
echo -e "${YELLOW}This may take a minute or two for the first run...${NC}"

ATTEMPTS=0
MAX_ATTEMPTS=30
while [ $ATTEMPTS -lt $MAX_ATTEMPTS ]; do
    if is_jenkins_running; then
        echo -e "${GREEN}Jenkins is up and running!${NC}"
        break
    fi

    ATTEMPTS=$((ATTEMPTS+1))
    echo -e "${YELLOW}Waiting for Jenkins to start... (Attempt $ATTEMPTS/$MAX_ATTEMPTS)${NC}"
    sleep 10
done

if [ $ATTEMPTS -eq $MAX_ATTEMPTS ]; then
    echo -e "${RED}Warning: Jenkins may not be fully started yet. Check logs with: brew services log jenkins-lts${NC}"
fi

# Display Jenkins URL and credentials
echo ""
echo -e "${GREEN}Jenkins is available at http://localhost:8080/${NC}"
echo -e "${GREEN}Username: admin${NC}"
echo -e "${GREEN}Password: admin${NC}"
echo -e "Initial admin password: ${GREEN}$(cat "$JENKINS_HOME/secrets/initialAdminPassword")${NC}"
echo ""
echo -e "${YELLOW}Jenkins logs can be viewed with: brew services log jenkins-lts${NC}"
echo ""

# Check if plugins need to be installed
if [ ! -d "$JENKINS_HOME/plugins" ] || [ -z "$(ls -A "$JENKINS_HOME/plugins")" ]; then
    echo -e "${YELLOW}No plugins detected. You may need to run ./install-plugins.sh after Jenkins has fully started.${NC}"
fi
