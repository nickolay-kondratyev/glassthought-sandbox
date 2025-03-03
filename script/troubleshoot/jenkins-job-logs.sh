#!/bin/bash

# Script to fetch logs for a specific Jenkins job
JOB_NAME="thorg-sanity-check"

echo "Fetching logs for job: $JOB_NAME"
echo "----------------------------------------"

# Get the last build number
BUILD_NUMBER=$(curl -s http://localhost:8080/job/$JOB_NAME/lastBuild/api/json | grep -o '"number":[0-9]*' | cut -d':' -f2)

if [ -z "$BUILD_NUMBER" ]; then
  echo "Could not determine the last build number. Check if Jenkins is running and the job exists."
  exit 1
fi

echo "Last build number: $BUILD_NUMBER"
echo "----------------------------------------"

# Get the build result
BUILD_RESULT=$(curl -s http://localhost:8080/job/$JOB_NAME/lastBuild/api/json | grep -o '"result":"[^"]*"' | cut -d'"' -f4)
echo "Build result: $BUILD_RESULT"
echo "----------------------------------------"

# Get the console output
echo "Console output:"
echo "----------------------------------------"
curl -s "http://localhost:8080/job/$JOB_NAME/$BUILD_NUMBER/consoleText"
echo "----------------------------------------"

# Check for specific errors in the console output
echo "Checking for common errors:"
curl -s "http://localhost:8080/job/$JOB_NAME/$BUILD_NUMBER/consoleText" | grep -i "error\|exception\|failure\|failed" | head -20 