#!/usr/bin/env bash

set -e

command -v brew || throw "Brew is not installed"
command -v java || throw "Java is not installed"
brew install jenkins-lts || throw "Failed to install Jenkins"

source "_env_setup_source_me.sh"

echo "--------------------------------------------------------------------------------"
echo -e "${GREEN:?}Starting Jenkins installation for macOS...${NC:?}"


# Create Jenkins directories
mkdir -p "${JENKINS_HOME:?}"
mkdir -p "${JENKINS_HOME:?}/casc_configs"
mkdir -p "${JENKINS_HOME:?}/init.groovy.d"

# Copy configuration files
echo -e "${YELLOW:?}Copying configuration files...${NC:?}"
cp jenkins-casc.yaml "${JENKINS_HOME:?}/casc_configs/jenkins.yaml"
cp -r init.groovy.d/* "${JENKINS_HOME:?}/init.groovy.d/"

# Set up SSH key for Jenkins
SSH_KEY_PATH=${SSH_KEY_PATH:-~/.ssh/id_rsa}
if [ -f "${SSH_KEY_PATH:?}" ]; then
    echo -e "${GREEN:?}Using SSH key from ${SSH_KEY_PATH:?}${NC:?}"
    mkdir -p "${JENKINS_HOME:?}/.ssh"
    cp "${SSH_KEY_PATH:?}" "${JENKINS_HOME:?}/.ssh/id_rsa"
    chmod 600 "${JENKINS_HOME:?}/.ssh/id_rsa"
    ssh-keyscan gitlab.com >> "${JENKINS_HOME:?}/.ssh/known_hosts"
else
    echo -e "${RED:?}Warning: SSH key not found at ${SSH_KEY_PATH:?}${NC:?}"
    echo -e "${YELLOW:?}Please provide a valid SSH key path with SSH_KEY_PATH environment variable${NC:?}"
fi

# Install Jenkins plugins
echo -e "${YELLOW:?}Installing Jenkins plugins...${NC:?}"

# Function to wait for Jenkins to start
wait_for_jenkins() {
    echo -e "${YELLOW:?}Waiting for Jenkins to start...${NC:?}"
    local attempts=0
    local max_attempts=30

    while [ ${attempts:?} -lt ${max_attempts:?} ]; do
        if curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/ | grep -q "200\|403"; then
            echo -e "${GREEN:?}Jenkins is up and running!${NC:?}"
            return 0
        fi

        attempts=$((attempts+1))
        echo -e "${YELLOW:?}Waiting for Jenkins to start... (Attempt ${attempts:?}/${max_attempts:?})${NC:?}"
        sleep 10
    done

    echo -e "${RED:?}Jenkins did not start within the expected time.${NC:?}"
    return 1
}

# Set environment variables for Jenkins
echo -e "${YELLOW:?}Setting up environment variables for Jenkins...${NC:?}"
cat > "${JENKINS_HOME:?}/jenkins.environment" << EOF
CASC_JENKINS_CONFIG=${JENKINS_HOME:?}/casc_configs/jenkins.yaml
JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
THORG_ROOT=${JENKINS_HOME:?}/workspace/thorg-root
EOF

echo -e "${GREEN:?}Jenkins installation completed!${NC:?}"
echo -e "${YELLOW:?}You can start Jenkins using the start-jenkins.sh script.${NC:?}"
echo -e "Jenkins home directory: ${GREEN:?}${JENKINS_HOME:?}${NC:?}"


