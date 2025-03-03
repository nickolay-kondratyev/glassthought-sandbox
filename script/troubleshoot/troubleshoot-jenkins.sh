#!/bin/bash

# Master script to run all Jenkins troubleshooting scripts
echo "Running Jenkins troubleshooting scripts"
echo "========================================"

# Make sure we're in the right directory
cd "$(dirname "$0")"

# Make all scripts executable
chmod +x jenkins-job-logs.sh
chmod +x jenkins-system-logs.sh
chmod +x jenkins-config-check.sh
chmod +x jenkins-docker-check.sh

# Create output directory
mkdir -p output

# Run each script and save output to files
echo "Running job logs check..."
./jenkins-job-logs.sh > output/job-logs.txt 2>&1
echo "Done. Results saved to output/job-logs.txt"

echo "Running system logs check..."
./jenkins-system-logs.sh > output/system-logs.txt 2>&1
echo "Done. Results saved to output/system-logs.txt"

echo "Running configuration check..."
./jenkins-config-check.sh > output/config-check.txt 2>&1
echo "Done. Results saved to output/config-check.txt"

echo "Running Docker check..."
./jenkins-docker-check.sh > output/docker-check.txt 2>&1
echo "Done. Results saved to output/docker-check.txt"

echo "========================================"
echo "All checks completed. Results saved to output directory:"
echo "  - output/job-logs.txt"
echo "  - output/system-logs.txt"
echo "  - output/config-check.txt"
echo "  - output/docker-check.txt"
echo ""
echo "To view a specific file, use: cat output/filename.txt"
echo "To search for errors in all files: grep -i error output/*.txt" 