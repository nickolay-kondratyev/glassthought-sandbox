#!/usr/bin/env bash

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Function to check if a command exists
command_exists() {
  command -v "$1" >/dev/null 2>&1
}

# Function to compare versions
version_gt() {
  test "$(printf '%s\n' "$1" "$2" | sort -V | head -n 1)" != "$1"
}

echo -e "${YELLOW}Verifying prerequisites for native Jenkins installation on macOS...${NC}"

# Check if Homebrew is installed
if ! command_exists brew; then
  echo -e "${RED}Error: Homebrew is not installed or not in PATH${NC}"
  echo -e "${YELLOW}Please install Homebrew: https://brew.sh/${NC}"
  exit 1
fi

echo -e "${GREEN}Homebrew is installed.${NC}"

# Check if Java is installed
if ! command_exists java; then
  echo -e "${RED}Error: Java is not installed or not in PATH${NC}"
  echo -e "${YELLOW}Please install Java using: brew install openjdk@17${NC}"
  exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F '.' '{print $1}')
echo -e "${GREEN}Java version: $JAVA_VERSION${NC}"

# Minimum required Java version
MIN_JAVA_VERSION="11"
if [ "$JAVA_VERSION" -lt "$MIN_JAVA_VERSION" ]; then
  echo -e "${RED}Error: Java version $JAVA_VERSION is too old${NC}"
  echo -e "${YELLOW}Please upgrade Java to version $MIN_JAVA_VERSION or newer${NC}"
  exit 1
fi

# Check if SSH key exists
SSH_KEY_PATH=${SSH_KEY_PATH:-~/.ssh/id_rsa}
if [ ! -f "$SSH_KEY_PATH" ]; then
  echo -e "${RED}Error: SSH key not found at $SSH_KEY_PATH${NC}"
  echo -e "${YELLOW}Please provide a valid SSH key path with SSH_KEY_PATH environment variable${NC}"
  exit 1
fi

echo -e "${GREEN}SSH key found at $SSH_KEY_PATH${NC}"

# Check if curl is installed
if ! command_exists curl; then
  echo -e "${RED}Error: curl is not installed or not in PATH${NC}"
  echo -e "${YELLOW}Please install curl using: brew install curl${NC}"
  exit 1
fi

echo -e "${GREEN}curl is installed.${NC}"

# Check if Jenkins is already installed
if brew list --formula | grep -q jenkins-lts; then
  echo -e "${GREEN}Jenkins is already installed.${NC}"
  JENKINS_VERSION=$(brew info jenkins-lts --json | grep -o '"installed":\["[^"]*' | cut -d'"' -f4)
  echo -e "${GREEN}Jenkins version: $JENKINS_VERSION${NC}"
else
  echo -e "${YELLOW}Jenkins is not installed. It will be installed by the install-jenkins.sh script.${NC}"
fi

# Check disk space
AVAILABLE_SPACE=$(df -h $HOME | awk 'NR==2 {print $4}')
echo -e "${GREEN}Available disk space: $AVAILABLE_SPACE${NC}"

echo -e "${GREEN}All prerequisites are satisfied!${NC}"
exit 0
