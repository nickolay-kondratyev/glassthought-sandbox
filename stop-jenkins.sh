#!/usr/bin/env bash

source "_env_setup_source_me.sh"

echo "--------------------------------------------------------------------------------"
echo -e "${YELLOW:?}Stopping Jenkins...${NC:?}"

# Check if Jenkins is running
if ! pgrep -f "jenkins.war" > /dev/null; then
    echo -e "${YELLOW:?}Jenkins is not running.${NC:?}"
    
    # Still try to unload the service in case it's in a weird state
    echo -e "${YELLOW:?}Ensuring service is unloaded...${NC:?}"
    brew services stop jenkins-lts 2>/dev/null || true
    launchctl unload ~/Library/LaunchAgents/homebrew.mxcl.jenkins-lts.plist 2>/dev/null || true
    
    exit 0
fi

# Stop Jenkins
echo -e "${YELLOW:?}Stopping Jenkins...${NC:?}"
brew services stop jenkins-lts

# Also try to unload the service directly
echo -e "${YELLOW:?}Unloading Jenkins service...${NC:?}"
launchctl unload ~/Library/LaunchAgents/homebrew.mxcl.jenkins-lts.plist 2>/dev/null || true

# Wait for Jenkins to stop
echo -e "${YELLOW:?}Waiting for Jenkins to stop...${NC:?}"
ATTEMPTS=0
MAX_ATTEMPTS=10
while [ $ATTEMPTS -lt $MAX_ATTEMPTS ]; do
    if ! pgrep -f "jenkins.war" > /dev/null; then
        echo -e "${GREEN:?}Jenkins has been stopped.${NC:?}"
        exit 0
    fi

    ATTEMPTS=$((ATTEMPTS+1))
    echo -e "${YELLOW:?}Waiting for Jenkins to stop... (Attempt $ATTEMPTS/$MAX_ATTEMPTS)${NC:?}"
    sleep 2
done

if [ $ATTEMPTS -eq $MAX_ATTEMPTS ]; then
    echo -e "${RED}Warning: Jenkins may not have stopped properly. Attempting to kill the process.${NC:?}"
    echo -e "${YELLOW:?}Killing Jenkins process...${NC:?}"
    pkill -f jenkins.war || true
    sleep 2
    
    if pgrep -f "jenkins.war" > /dev/null; then
        echo -e "${RED}Failed to kill Jenkins process. You may need to kill it manually:${NC:?}"
        echo -e "${YELLOW:?}pkill -9 -f jenkins.war${NC:?}"
        exit 1
    else
        echo -e "${GREEN:?}Jenkins process has been killed.${NC:?}"
    fi
fi
