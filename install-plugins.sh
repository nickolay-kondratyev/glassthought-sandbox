#!/usr/bin/env bash

set -e

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

JENKINS_HOME="$HOME/.jenkins"
JENKINS_WAR=$(brew --prefix)/opt/jenkins-lts/libexec/jenkins.war
JENKINS_CLI_JAR="$JENKINS_HOME/jenkins-cli.jar"
JENKINS_URL="http://localhost:8080"

# Function to check if Jenkins is running
is_jenkins_running() {
    curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/ | grep -q "200\|403"
    return $?
}

# Download Jenkins CLI if not already present
download_jenkins_cli() {
    if [ ! -f "$JENKINS_CLI_JAR" ]; then
        echo -e "${YELLOW}Downloading Jenkins CLI...${NC}"
        curl -s -o "$JENKINS_CLI_JAR" "$JENKINS_URL/jnlpJars/jenkins-cli.jar"
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
    
    echo -e "${RED}Jenkins did not start within the expected time.${NC}"
    return 1
}

# Install plugins from plugins.txt
install_plugins() {
    echo -e "${YELLOW}Installing Jenkins plugins...${NC}"
    
    # Check if plugins.txt exists
    if [ ! -f "plugins.txt" ]; then
        echo -e "${RED}Error: plugins.txt not found${NC}"
        return 1
    fi
    
    # Download Jenkins CLI
    download_jenkins_cli
    
    # Get initial admin password
    local admin_password="admin"
    
    # Install each plugin
    while read -r plugin; do
        if [ -n "$plugin" ] && [[ ! "$plugin" =~ ^# ]]; then
            echo -e "${YELLOW}Installing plugin: $plugin${NC}"
            java -jar "$JENKINS_CLI_JAR" -s "$JENKINS_URL" -auth admin:$admin_password install-plugin "$plugin" -deploy
        fi
    done < plugins.txt
    
    echo -e "${GREEN}All plugins installed successfully!${NC}"
    
    # Restart Jenkins to apply plugin changes
    echo -e "${YELLOW}Restarting Jenkins to apply plugin changes...${NC}"
    java -jar "$JENKINS_CLI_JAR" -s "$JENKINS_URL" -auth admin:$admin_password safe-restart
    
    # Wait for Jenkins to come back up
    sleep 10
    wait_for_jenkins
}

# Main execution
if is_jenkins_running; then
    install_plugins
else
    echo -e "${RED}Error: Jenkins is not running. Please start Jenkins first.${NC}"
    exit 1
fi 