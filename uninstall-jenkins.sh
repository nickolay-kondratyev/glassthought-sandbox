#!/usr/bin/env bash

source "_env_setup_source_me.sh"


# Check if Jenkins is installed
if ! brew list --formula | grep -q jenkins-lts; then
    echo -e "${YELLOW:?}Jenkins is not installed via Homebrew.${NC:?}"
else
    # Check if Jenkins is running
    if pgrep -f "jenkins.war" > /dev/null; then
        echo -e "${YELLOW:?}Jenkins is currently running. Stopping Jenkins...${NC:?}"
        ./stop-jenkins.sh || brew services stop jenkins-lts

        # Wait a moment to ensure Jenkins is fully stopped
        sleep 5
    fi

    # Uninstall Jenkins
    echo -e "${YELLOW:?}Uninstalling Jenkins...${NC:?}"
    brew uninstall jenkins-lts
    echo -e "${GREEN:?}Jenkins has been uninstalled from Homebrew.${NC:?}"
fi

# Ask if user wants to remove Jenkins home directory
read -p "Do you want to remove the Jenkins home directory ($JENKINS_HOME)? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    # Remove Jenkins home
    echo -e "${YELLOW:?}Removing Jenkins home directory...${NC:?}"
    rm -rf "$JENKINS_HOME"
    echo -e "${GREEN:?}Jenkins home directory has been removed.${NC:?}"
    echo -e "${YELLOW:?}A backup of your Jenkins home has been saved to $BACKUP_DIR${NC:?}"
else
    echo -e "${YELLOW:?}Jenkins home directory has been preserved.${NC:?}"
fi

echo -e "${GREEN:?}Jenkins uninstallation completed!${NC:?}"
