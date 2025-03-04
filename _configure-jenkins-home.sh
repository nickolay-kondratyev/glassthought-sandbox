#!/usr/bin/env bash

# File: _configure-jenkins-home.sh
# Usage: Run with sudo ./_configure-jenkins-home.sh

set -e

source "_env_setup_source_me.sh"

# Determine Homebrew prefix based on architecture
BREW_PREFIX=$(brew --prefix)
PLIST_PATH="$BREW_PREFIX/opt/jenkins-lts/homebrew.mxcl.jenkins-lts.plist"

# Verify plist existence
if [ ! -f "$PLIST_PATH" ]; then
    echo -e "\n${RED}Error: Jenkins plist not found at expected location:${NC}"
    echo "Searched in: $PLIST_PATH"
    echo -e "\n${YELLOW}Try alternative locations:${NC}"
    echo "1. Check if installed: brew list jenkins-lts"
    echo "2. Find actual plist: brew services list | grep jenkins-lts"
    exit 1
fi

# Check if running as root
if [ "$EUID" -ne 0 ]; then
    echo -e "${RED}Please run this script with sudo${NC}"
    exit 1
fi

# Stop Jenkins if running
echo -e "${YELLOW}Stopping Jenkins...${NC}"
brew services stop jenkins-lts || true

# Create backup of plist
BACKUP_PLIST="${PLIST_PATH}.bak_$(date +%s)"
echo -e "${YELLOW}Creating plist backup at ${BACKUP_PLIST}${NC}"
cp "$PLIST_PATH" "$BACKUP_PLIST"

# Use PlistBuddy to modify the plist
echo -e "${YELLOW}Modifying plist at $PLIST_PATH${NC}"

# Clean existing environment variables if needed
/usr/libexec/PlistBuddy -c "Delete :EnvironmentVariables" "$PLIST_PATH" 2>/dev/null || true

# Add new environment variables
/usr/libexec/PlistBuddy -c "Add :EnvironmentVariables dict" "$PLIST_PATH"
/usr/libexec/PlistBuddy -c "Add :EnvironmentVariables:JENKINS_HOME string $JENKINS_HOME" "$PLIST_PATH"
/usr/libexec/PlistBuddy -c "Add :EnvironmentVariables:CASC_JENKINS_CONFIG string $JENKINS_HOME/casc_configs/jenkins.yaml" "$PLIST_PATH"
/usr/libexec/PlistBuddy -c "Add :EnvironmentVariables:JAVA_OPTS string -Djenkins.install.runSetupWizard=false" "$PLIST_PATH"

# Reload service configuration
echo -e "${YELLOW}Reloading service configuration...${NC}"
launchctl unload "$PLIST_PATH" 2>/dev/null || true
launchctl load -w "$PLIST_PATH"

echo -e "\n${GREEN}Jenkins configuration updated successfully!${NC}"
echo -e "New settings in plist:"
/usr/libexec/PlistBuddy -c "Print :EnvironmentVariables" "$PLIST_PATH"

echo -e "\n${YELLOW}You can now start Jenkins with:${NC}"
echo "brew services start jenkins-lts"
