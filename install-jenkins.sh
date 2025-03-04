#!/usr/bin/env bash

set -e

include "_env_variables_source_me.sh"

echo -e "${GREEN:?}Starting Jenkins installation for macOS...${NC:?}"

# Check if Homebrew is installed
if ! command -v brew &> /dev/null; then
    echo -e "${YELLOW:?}Homebrew not found. Installing Homebrew...${NC:?}"
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
else
    echo -e "${GREEN:?}Homebrew is already installed.${NC:?}"
    brew update
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo -e "${YELLOW:?}Java not found. Installing OpenJDK 17...${NC:?}"
    brew install openjdk@17

    # Create a symlink to make Java available system-wide
    sudo ln -sfn $(brew --prefix)/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk

    # Add Java to PATH if not already there
    if ! grep -q 'export PATH="$PATH:/opt/homebrew/opt/openjdk@17/bin"' ~/.zshrc 2>/dev/null && ! grep -q 'export PATH="$PATH:/opt/homebrew/opt/openjdk@17/bin"' ~/.bash_profile 2>/dev/null; then
        if [ -f ~/.zshrc ]; then
            echo 'export PATH="$PATH:/opt/homebrew/opt/openjdk@17/bin"' >> ~/.zshrc
            source ~/.zshrc
        elif [ -f ~/.bash_profile ]; then
            echo 'export PATH="$PATH:/opt/homebrew/opt/openjdk@17/bin"' >> ~/.bash_profile
            source ~/.bash_profile
        fi
    fi
else
    echo -e "${GREEN:?}Java is already installed.${NC:?}"
    java -version
fi

# Install Jenkins
if ! brew list --formula | grep -q jenkins; then
    echo -e "${YELLOW:?}Installing Jenkins...${NC:?}"
    brew install jenkins-lts
else
    echo -e "${GREEN:?}Jenkins is already installed.${NC:?}"
    brew upgrade jenkins-lts
fi

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
JENKINS_CLI_JAR="${JENKINS_HOME:?}/jenkins-cli.jar"

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
