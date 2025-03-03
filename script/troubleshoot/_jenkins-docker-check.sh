#!/bin/bash

# Script to check Docker-related issues for Jenkins
echo "Checking Docker configuration for Jenkins"
echo "----------------------------------------"

# Use credentials from environment or default to admin:admin
JENKINS_USER=${JENKINS_USER:-admin}
JENKINS_PASS=${JENKINS_PASS:-admin}
AUTH_HEADER="Authorization: Basic $(echo -n $JENKINS_USER:$JENKINS_PASS | base64)"

# Check if Docker is running
if ! docker info >/dev/null 2>&1; then
  echo "Docker is not running or the current user doesn't have permission to use Docker."
  exit 1
fi

echo "Docker is running."

# Check if Jenkins container is running
JENKINS_CONTAINER=$(docker ps | grep jenkins | awk '{print $1}')

if [ -z "$JENKINS_CONTAINER" ]; then
  echo "No Jenkins container found running."
  echo "Checking for stopped Jenkins containers:"
  docker ps -a | grep jenkins
  
  # Check if Jenkins is running as a service instead
  if systemctl is-active jenkins >/dev/null 2>&1; then
    echo "Jenkins is running as a system service."
  fi
else
  echo "Jenkins container is running: $JENKINS_CONTAINER"
  
  # Check container logs
  echo "----------------------------------------"
  echo "Last 50 lines of Jenkins container logs:"
  docker logs --tail 50 $JENKINS_CONTAINER
  
  # Check if Docker socket is mounted (for Docker-in-Docker)
  echo "----------------------------------------"
  echo "Checking Docker socket mount:"
  docker inspect $JENKINS_CONTAINER | grep -A 3 "Mounts" | grep docker.sock
  
  # Check Docker network
  echo "----------------------------------------"
  echo "Docker network configuration:"
  docker network inspect $(docker inspect --format='{{range .NetworkSettings.Networks}}{{.NetworkID}}{{end}}' $JENKINS_CONTAINER)
fi

# Check if Docker plugin is configured in Jenkins
echo "----------------------------------------"
echo "Checking Docker plugin configuration in Jenkins:"
curl -s -H "$AUTH_HEADER" http://localhost:8080/configure | grep -A 10 -B 10 "docker"

# Check if Docker agent is configured
echo "----------------------------------------"
echo "Checking Docker agent configuration:"
curl -s -H "$AUTH_HEADER" http://localhost:8080/computer/api/json | grep -i "docker"

# Check if the job is configured to use Docker
echo "----------------------------------------"
echo "Checking if job uses Docker:"
JOB_NAME="thorg-sanity-check"
JOB_CONFIG=$(curl -s -H "$AUTH_HEADER" http://localhost:8080/job/$JOB_NAME/config.xml)
echo "$JOB_CONFIG" | grep -A 10 -B 10 "docker"

# Return success if Docker is running
if docker info >/dev/null 2>&1; then
  exit 0
else
  exit 1
fi 