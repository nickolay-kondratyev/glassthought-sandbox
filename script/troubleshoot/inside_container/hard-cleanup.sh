#!/bin/bash

# complete-cleanup.sh - Script to completely clean Jenkins and start fresh

function throw() {
    local message="${1:?Error message required}"
    echo "ERROR: ${message}" >&2
    exit 1
}

echo "=== JENKINS COMPLETE CLEANUP ==="
echo "This script will completely remove the Jenkins container and volume"
echo "and start fresh with a clean installation."
echo ""
echo "WARNING: All Jenkins configuration and data will be lost!"
echo "Continue? (y/n)"
read -r response
if [[ "$response" != "y" ]]; then
    echo "Operation cancelled."
    exit 0
fi

# Stop all containers
echo "Stopping all containers..."
docker-compose down
if [ $? -ne 0 ]; then
    throw "Failed to stop containers. Please check docker-compose configuration."
fi

# List all volumes
echo "Listing volumes that will be removed..."
JENKINS_VOLUMES=$(docker volume ls -q | grep "jenkins")
echo "${JENKINS_VOLUMES}"

# Remove all Jenkins volumes
echo "Removing Jenkins volumes..."
for volume in ${JENKINS_VOLUMES}; do
    echo "Removing volume: ${volume}"
    docker volume rm "${volume}"
done

# Remove any lingering containers
echo "Removing any lingering Jenkins containers..."
JENKINS_CONTAINERS=$(docker ps -a | grep "jenkins" | awk '{print $1}')
if [ -n "${JENKINS_CONTAINERS}" ]; then
    docker rm -f ${JENKINS_CONTAINERS}
fi

# Verify jenkins-casc.yaml has the correct branch
echo "Checking jenkins-casc.yaml for correct branch configuration..."
if [ -f "jenkins-casc.yaml" ]; then
    if grep -q "branches('\*/main')" jenkins-casc.yaml; then
        echo "Fixing branch reference in jenkins-casc.yaml..."
        sed -i "s/branches('\*\/main')/branches('*\/master')/g" jenkins-casc.yaml
    fi

    # Check for the syntax error in triggers
    if grep -q "scm('H/15 \* '\*)" jenkins-casc.yaml; then
        echo "Fixing syntax error in triggers configuration..."
        sed -i "s/scm('H\/15 \* '\*)/scm('H\/15 * * * *')/g" jenkins-casc.yaml
    fi

    echo "Current branch configuration in jenkins-casc.yaml:"
    grep -n "branches" jenkins-casc.yaml
else
    echo "jenkins-casc.yaml not found in current directory."
fi

# Rebuild and start Jenkins
echo "Building and starting fresh Jenkins container..."
docker-compose build --no-cache
if [ $? -ne 0 ]; then
    throw "Failed to build Jenkins container."
fi

docker-compose up -d
if [ $? -ne 0 ]; then
    throw "Failed to start Jenkins container."
fi

echo "Jenkins container started. Waiting for it to initialize..."
sleep 10

# Follow logs to see startup progress
echo "Displaying Jenkins logs (Ctrl+C to exit logs but keep Jenkins running):"
docker-compose logs -f jenkins
