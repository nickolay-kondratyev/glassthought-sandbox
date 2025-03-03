#!/bin/bash

# Script to check Jenkins configuration
JOB_NAME="thorg-sanity-check"

echo "Checking Jenkins configuration for job: $JOB_NAME"
echo "----------------------------------------"

# Check if the job exists
JOB_EXISTS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/job/$JOB_NAME/api/json)

if [ "$JOB_EXISTS" != "200" ]; then
  echo "Job $JOB_NAME does not exist or cannot be accessed."
  exit 1
fi

# Get job configuration
echo "Job configuration (config.xml):"
echo "----------------------------------------"
curl -s http://localhost:8080/job/$JOB_NAME/config.xml | xmllint --format -

# Check installed plugins
echo "----------------------------------------"
echo "Installed plugins:"
curl -s http://localhost:8080/pluginManager/api/json?depth=1 | 
  grep -o '"shortName":"[^"]*","longName":"[^"]*","version":"[^"]*"' | 
  sed 's/"shortName":"\([^"]*\)","longName":"\([^"]*\)","version":"\([^"]*\)"/\1 (\3)/'

# Check system info
echo "----------------------------------------"
echo "System information:"
curl -s http://localhost:8080/systemInfo | grep -A 2 -B 2 "java\|memory\|user" 