#!/bin/bash

# Script to check Jenkins configuration
JOB_NAME="thorg-sanity-check"

echo "Checking Jenkins configuration for job: $JOB_NAME"
echo "----------------------------------------"

# Use credentials from environment or default to admin:admin
JENKINS_USER=${JENKINS_USER:-admin}
JENKINS_PASS=${JENKINS_PASS:-admin}
AUTH_HEADER="Authorization: Basic $(echo -n $JENKINS_USER:$JENKINS_PASS | base64)"

# Check if the job exists
JOB_EXISTS=$(curl -s -H "$AUTH_HEADER" -o /dev/null -w "%{http_code}" http://localhost:8080/job/$JOB_NAME/api/json)

if [ "$JOB_EXISTS" != "200" ]; then
  echo "Job $JOB_NAME does not exist or cannot be accessed."
  exit 1
fi

# Get job configuration
echo "Job configuration (config.xml):"
echo "----------------------------------------"
JOB_CONFIG=$(curl -s -H "$AUTH_HEADER" http://localhost:8080/job/$JOB_NAME/config.xml)
echo "$JOB_CONFIG" | xmllint --format - 2>/dev/null || echo "$JOB_CONFIG"

# Check Git configuration
echo "----------------------------------------"
echo "Git configuration:"
echo "$JOB_CONFIG" | grep -A 10 -B 10 "GitSCM" | grep -i "url\|branch\|credential"

# Check SSH key configuration
echo "----------------------------------------"
echo "SSH key configuration:"
curl -s -H "$AUTH_HEADER" http://localhost:8080/credentials/store/system/domain/_/api/json | grep -i "ssh\|key\|git"

# Check installed plugins
echo "----------------------------------------"
echo "Installed plugins (Git related):"
curl -s -H "$AUTH_HEADER" http://localhost:8080/pluginManager/api/json?depth=1 | 
  grep -o '"shortName":"[^"]*","longName":"[^"]*","version":"[^"]*"' | 
  grep -i "git\|ssh\|credential" |
  sed 's/"shortName":"\([^"]*\)","longName":"\([^"]*\)","version":"\([^"]*\)"/\1 (\3)/'

# Check system info
echo "----------------------------------------"
echo "System information:"
curl -s -H "$AUTH_HEADER" http://localhost:8080/systemInfo | grep -A 2 -B 2 "java\|memory\|user"

# Return success if job exists and we could get config
if [ "$JOB_EXISTS" == "200" ] && [ -n "$JOB_CONFIG" ]; then
  exit 0
else
  exit 1
fi 