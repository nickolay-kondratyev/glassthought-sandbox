source "_env_setup_source_me.sh"

# Check if running as root
if [ "$EUID" -ne 0 ]; then
    echo -e "${RED}Please run this script with sudo${NC}"
    exit 1
fi

# Use PlistBuddy to modify the plist
echo -e "${YELLOW}Modifying plist at $JENKINS_PLIST_PATH${NC}"

# Clean existing environment variables if needed
/usr/libexec/PlistBuddy -c "Delete :EnvironmentVariables" "$JENKINS_PLIST_PATH" 2>/dev/null || true

# Add new environment variables
/usr/libexec/PlistBuddy -c "Add :EnvironmentVariables dict" "$JENKINS_PLIST_PATH"
/usr/libexec/PlistBuddy -c "Add :EnvironmentVariables:JENKINS_HOME string $JENKINS_HOME" "$JENKINS_PLIST_PATH"
/usr/libexec/PlistBuddy -c "Add :EnvironmentVariables:CASC_JENKINS_CONFIG string $JENKINS_HOME/casc_configs/jenkins.yaml" "$JENKINS_PLIST_PATH"
/usr/libexec/PlistBuddy -c "Add :EnvironmentVariables:JAVA_OPTS string -Djenkins.install.runSetupWizard=false" "$JENKINS_PLIST_PATH"

# Reload service configuration
echo -e "${YELLOW}Reloading service configuration...${NC}"
launchctl unload "$JENKINS_PLIST_PATH" 2>/dev/null || true
echo "Starting load [launchctl load -w $JENKINS_PLIST_PATH]"
launchctl load -w "$JENKINS_PLIST_PATH" || throw "Failed to reload service configuration"

echo -e "\n${GREEN}Jenkins configuration updated successfully!${NC}"
echo -e "New settings in plist:"
/usr/libexec/PlistBuddy -c "Print :EnvironmentVariables" "$JENKINS_PLIST_PATH"

echo -e "\n${GREEN}Completed PList modifications.${NC}"
echo ""

