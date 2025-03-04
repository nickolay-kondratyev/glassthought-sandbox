#!/usr/bin/env bash

# jenkins-cleanup.sh - Script to completely clean Jenkins (without restart)


echo "=== JENKINS COMPLETE CLEANUP ==="
echo "This script will stop and completely remove the Jenkins container and volumes."
echo "You will need to manually restart Jenkins after this cleanup."
echo ""
echo "WARNING: All Jenkins configuration and data will be lost!"

# Stopping jenkins container
eai ./stop-jenkins.sh

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

echo "=== CLEANUP COMPLETE ==="
echo ""
echo "All Jenkins containers and volumes have been removed."
echo "Before restarting Jenkins, you may want to check your configuration files:"
echo ""
echo "1. Verify jenkins-casc.yaml has the correct branch configuration:"
echo "   grep -n \"branches\" jenkins-casc.yaml"
echo ""
echo "2. Check for syntax errors in triggers:"
echo "   grep -n \"triggers\" jenkins-casc.yaml"
echo ""
echo "To restart Jenkins after verifying configurations:"
echo "   ./start-jenkins.sh"
