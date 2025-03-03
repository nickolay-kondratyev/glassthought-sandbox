#!/bin/bash

# Script to check Jenkins system logs
echo "Checking Jenkins system logs"
echo "----------------------------------------"

# Use credentials from environment or default to admin:admin
JENKINS_USER=${JENKINS_USER:-admin}
JENKINS_PASS=${JENKINS_PASS:-admin}
AUTH_HEADER="Authorization: Basic $(echo -n $JENKINS_USER:$JENKINS_PASS | base64)"

# Try to get logs from Jenkins API first
echo "Fetching logs from Jenkins API:"
LOGS=$(curl -s -H "$AUTH_HEADER" "http://localhost:8080/log/all" | head -50)

if [ -n "$LOGS" ]; then
  echo "$LOGS"
else
  echo "Could not fetch logs from API, trying alternative methods..."
  
  # If Jenkins is running in Docker, adjust this command
  if [ -f "/var/log/jenkins/jenkins.log" ]; then
    echo "Last 50 lines of Jenkins system log:"
    tail -50 /var/log/jenkins/jenkins.log
  elif [ -d "/var/jenkins_home/logs" ]; then
    echo "Last 50 lines of Jenkins logs in Docker:"
    docker exec -it jenkins tail -50 /var/jenkins_home/logs/jenkins.log
  else
    echo "Could not locate Jenkins logs. Please check your Jenkins installation."
    echo "Common locations:"
    echo "  - /var/log/jenkins/jenkins.log"
    echo "  - /var/jenkins_home/logs/jenkins.log (Docker)"
    echo "  - ~/.jenkins/logs/jenkins.log (local installation)"
    exit 1
  fi
fi

# Check for errors in the logs
echo "----------------------------------------"
echo "Checking for errors in logs:"
echo "$LOGS" | grep -i "error\|exception\|failure\|failed\|warning\|critical" | head -10

# Check for startup issues
echo "----------------------------------------"
echo "Checking for startup issues:"
echo "$LOGS" | grep -i "init\|start\|boot\|launch" | head -10

# Return success if we got logs
if [ -n "$LOGS" ]; then
  exit 0
else
  exit 1
fi 