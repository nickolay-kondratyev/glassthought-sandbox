#!/usr/bin/env bash

set -e

source "_env_setup_source_me.sh"

# Check if Jenkins is installed
if ! brew list --formula | grep -q jenkins-lts; then
    echo -e "${RED}Error: Jenkins is not installed.${NC:?}"
    echo -e "${YELLOW:?}Please run ./install-jenkins.sh first.${NC:?}"
    exit 1
fi

# Check if Jenkins is running
if pgrep -f "jenkins.war" > /dev/null; then
    echo -e "${YELLOW:?}Jenkins is currently running. Stopping Jenkins before update...${NC:?}"
    ./stop-jenkins.sh

    # Wait a moment to ensure Jenkins is fully stopped
    sleep 5
fi

# Update Jenkins
echo -e "${YELLOW:?}Updating Jenkins...${NC:?}"
brew update
brew upgrade jenkins-lts

# Backup existing configuration
echo -e "${YELLOW:?}Backing up existing configuration...${NC:?}"
BACKUP_DIR="$JENKINS_HOME/backups/$(date +%Y%m%d_%H%M%S)"
mkdir -p "$BACKUP_DIR"
cp -r "$JENKINS_HOME/casc_configs" "$BACKUP_DIR/" 2>/dev/null || true
cp -r "$JENKINS_HOME/init.groovy.d" "$BACKUP_DIR/" 2>/dev/null || true
cp "$JENKINS_HOME/jenkins.environment" "$BACKUP_DIR/" 2>/dev/null || true

echo -e "${GREEN:?}Jenkins has been updated successfully!${NC:?}"
echo -e "${YELLOW:?}You can start Jenkins using ./start-jenkins.sh${NC:?}"
echo -e "${YELLOW:?}A backup of your configuration has been saved to $BACKUP_DIR${NC:?}"
