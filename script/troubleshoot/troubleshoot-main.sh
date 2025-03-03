#!/bin/bash

# Master script to run all Jenkins troubleshooting scripts
echo "====================================================="
echo "Jenkins Troubleshooting Tool"
echo "====================================================="

# Make sure we're in the right directory
cd "$(dirname "$0")"

# Make all scripts executable
chmod +x _jenkins-job-logs.sh
chmod +x _jenkins-system-logs.sh
chmod +x _jenkins-config-check.sh
chmod +x _jenkins-docker-check.sh

# Create output directory
mkdir -p output

# Set Jenkins credentials
JENKINS_USER="admin"
JENKINS_PASS="admin"
export JENKINS_USER JENKINS_PASS

# Run each script and save output to files
echo "Running job logs check..."
./_jenkins-job-logs.sh > output/job-logs.txt 2>&1
JOB_STATUS=$?

echo "Running system logs check..."
./_jenkins-system-logs.sh > output/system-logs.txt 2>&1
SYSTEM_STATUS=$?

echo "Running configuration check..."
./_jenkins-config-check.sh > output/config-check.txt 2>&1
CONFIG_STATUS=$?

echo "Running Docker check..."
./_jenkins-docker-check.sh > output/docker-check.txt 2>&1
DOCKER_STATUS=$?

# Display a summary of findings
echo "====================================================="
echo "TROUBLESHOOTING SUMMARY"
echo "====================================================="

# Function to print status
print_status() {
  if [ $1 -eq 0 ]; then
    echo "✅ $2: No issues detected"
  else
    echo "❌ $2: Issues detected (see details below)"
  fi
}

print_status $JOB_STATUS "Job Configuration"
print_status $SYSTEM_STATUS "System Logs"
print_status $CONFIG_STATUS "Jenkins Configuration"
print_status $DOCKER_STATUS "Docker Integration"

echo ""
echo "====================================================="
echo "DETAILED FINDINGS"
echo "====================================================="

# Function to display key findings from each file
display_findings() {
  echo "🔍 $1:"
  if [ -f "output/$2" ]; then
    # Extract error messages and important information
    grep -i "error\|exception\|failure\|failed\|warning\|critical" "output/$2" | head -5
    
    # If no errors found, show a positive message
    if [ $? -ne 0 ]; then
      echo "   No critical issues found in $2"
    fi
  else
    echo "   Output file not found"
  fi
  echo ""
}

display_findings "Job Logs" "job-logs.txt"
display_findings "System Logs" "system-logs.txt"
display_findings "Configuration" "config-check.txt"
display_findings "Docker" "docker-check.txt"

echo "====================================================="
echo "RECOMMENDATIONS"
echo "====================================================="

# Provide recommendations based on findings
if [ $JOB_STATUS -ne 0 ]; then
  echo "- Check job configuration and permissions"
  echo "- Verify Git repository access and branch settings"
fi

if [ $SYSTEM_STATUS -ne 0 ]; then
  echo "- Review Jenkins system logs for startup issues"
  echo "- Check for permission problems or resource constraints"
fi

if [ $CONFIG_STATUS -ne 0 ]; then
  echo "- Verify Jenkins plugin compatibility"
  echo "- Check SSH key configuration for Git access"
fi

if [ $DOCKER_STATUS -ne 0 ]; then
  echo "- Ensure Docker daemon is running"
  echo "- Verify Jenkins has proper permissions to access Docker"
fi

echo ""
echo "====================================================="
echo "All detailed results saved to output directory:"
echo "  - output/job-logs.txt"
echo "  - output/system-logs.txt"
echo "  - output/config-check.txt"
echo "  - output/docker-check.txt"
echo ""
echo "To view a specific file, use: cat output/filename.txt"
echo "To search for errors in all files: grep -i error output/*.txt"
echo "=====================================================" 