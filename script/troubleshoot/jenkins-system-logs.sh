#!/bin/bash

# Script to check Jenkins system logs
echo "Checking Jenkins system logs"
echo "----------------------------------------"

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
fi 