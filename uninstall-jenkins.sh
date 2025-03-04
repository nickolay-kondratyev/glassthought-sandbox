#!/usr/bin/env bash

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

JENKINS_HOME="$HOME/.jenkins"

# Check if Jenkins is installed
if ! brew list --formula | grep -q jenkins-lts; then
    echo -e "${YELLOW}Jenkins is not installed via Homebrew.${NC}"
else
    # Check if Jenkins is running
    if pgrep -f "jenkins.war" > /dev/null; then
        echo -e "${YELLOW}Jenkins is currently running. Stopping Jenkins...${NC}"
        ./stop-jenkins.sh || brew services stop jenkins-lts
        
        # Wait a moment to ensure Jenkins is fully stopped
        sleep 5
    fi
    
    # Uninstall Jenkins
    echo -e "${YELLOW}Uninstalling Jenkins...${NC}"
    brew uninstall jenkins-lts
    echo -e "${GREEN}Jenkins has been uninstalled from Homebrew.${NC}"
fi

# Ask if user wants to remove Jenkins home directory
read -p "Do you want to remove the Jenkins home directory ($JENKINS_HOME)? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    # Backup Jenkins home before removing
    BACKUP_DIR="$HOME/jenkins_backup_$(date +%Y%m%d_%H%M%S)"
    echo -e "${YELLOW}Backing up Jenkins home to $BACKUP_DIR...${NC}"
    mkdir -p "$BACKUP_DIR"
    cp -r "$JENKINS_HOME" "$BACKUP_DIR/"
    
    # Remove Jenkins home
    echo -e "${YELLOW}Removing Jenkins home directory...${NC}"
    rm -rf "$JENKINS_HOME"
    echo -e "${GREEN}Jenkins home directory has been removed.${NC}"
    echo -e "${YELLOW}A backup of your Jenkins home has been saved to $BACKUP_DIR${NC}"
else
    echo -e "${YELLOW}Jenkins home directory has been preserved.${NC}"
fi

echo -e "${GREEN}Jenkins uninstallation completed!${NC}" 