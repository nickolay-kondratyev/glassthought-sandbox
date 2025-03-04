#!/usr/bin/env bash

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Check if Jenkins is running
if ! pgrep -f "jenkins.war" > /dev/null; then
    echo -e "${YELLOW}Jenkins is not running.${NC}"
    exit 0
fi

# Stop Jenkins
echo -e "${YELLOW}Stopping Jenkins...${NC}"
brew services stop jenkins-lts

# Wait for Jenkins to stop
echo -e "${YELLOW}Waiting for Jenkins to stop...${NC}"
ATTEMPTS=0
MAX_ATTEMPTS=10
while [ $ATTEMPTS -lt $MAX_ATTEMPTS ]; do
    if ! pgrep -f "jenkins.war" > /dev/null; then
        echo -e "${GREEN}Jenkins has been stopped.${NC}"
        exit 0
    fi
    
    ATTEMPTS=$((ATTEMPTS+1))
    echo -e "${YELLOW}Waiting for Jenkins to stop... (Attempt $ATTEMPTS/$MAX_ATTEMPTS)${NC}"
    sleep 2
done

if [ $ATTEMPTS -eq $MAX_ATTEMPTS ]; then
    echo -e "${RED}Warning: Jenkins may not have stopped properly. You may need to kill the process manually.${NC}"
    echo -e "${YELLOW}You can use: pkill -f jenkins.war${NC}"
    exit 1
fi
