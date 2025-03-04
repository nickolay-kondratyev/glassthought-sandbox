#!/usr/bin/env bash

# File: _configure-jenkins-home.sh
# Usage: Run with sudo ./_configure-jenkins-home.sh

set -e

source "_env_setup_source_me.sh"

main_not_under_sudo() {
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

  # Stop Jenkins if running
  echo -e "${YELLOW}Stopping Jenkins...${NC}"
  brew services stop jenkins-lts || throw "Failed to stop Jenkins"
}


main() {
  main_not_under_sudo

  (sudo "./_configure-jenkins-home.sudo-portion.sh")
}

main "${@}" || exit 1

