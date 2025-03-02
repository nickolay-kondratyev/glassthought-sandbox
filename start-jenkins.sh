#!/bin/bash

# Verify Docker and Docker Compose are installed
echo "Verifying prerequisites..."
./verify-prerequisites.sh

# Set default SSH key path if not provided
export SSH_KEY_PATH=${SSH_KEY_PATH:-~/.ssh/id_rsa}

# Check if SSH key exists
if [ ! -f "$SSH_KEY_PATH" ]; then
  echo "Error: SSH key not found at $SSH_KEY_PATH"
  echo "Please provide a valid SSH key path with SSH_KEY_PATH environment variable"
  throw "SSH key not found at $SSH_KEY_PATH"
  exit 1
fi

# Export SSH private key content if needed for direct entry in Jenkins
if [ -z "$SSH_PRIVATE_KEY" ]; then
  export SSH_PRIVATE_KEY=$(cat $SSH_KEY_PATH)
fi

# Build and start Jenkins container
docker-compose up -d --build

# Wait for Jenkins to start
echo "Waiting for Jenkins to start..."
sleep 10

# Display Jenkins URL and initial admin password
echo "Jenkins is starting at http://localhost:8080/"
echo "Username: admin"
echo "Password: admin"
echo ""
echo "Jenkins logs can be viewed with: docker-compose logs -f jenkins"
