#!/usr/bin/env bash

# Verify Docker and Docker Compose are installed
echo "Verifying prerequisites..."
./verify-prerequisites.sh
if [ $? -ne 0 ]; then
  echo "Prerequisites check failed. Please fix the issues and try again."
  exit 1
fi

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
echo "Building and starting Jenkins container..."
docker-compose build --no-cache
if [ $? -ne 0 ]; then
  echo "Error: Failed to build Jenkins container."
  echo "Check the build logs above for more details."
  throw "Failed to build Jenkins container"
  exit 1
fi

docker-compose up -d
if [ $? -ne 0 ]; then
  echo "Error: Failed to start Jenkins container."
  echo "Check the logs with: docker-compose logs jenkins"
  throw "Failed to start Jenkins container"
  exit 1
fi

# Wait for Jenkins to start
echo "Waiting for Jenkins to start..."
echo "This may take a minute or two for the first run..."

# Function to check if Jenkins is up
check_jenkins() {
  docker-compose exec jenkins curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/login
}

# Wait for Jenkins to be ready
ATTEMPTS=0
MAX_ATTEMPTS=30
while [ $ATTEMPTS -lt $MAX_ATTEMPTS ]; do
  STATUS=$(check_jenkins 2>/dev/null || echo "000")

  if [ "$STATUS" = "200" ] || [ "$STATUS" = "403" ]; then
    echo "Jenkins is up and running!"
    break
  fi

  ATTEMPTS=$((ATTEMPTS+1))
  echo "Waiting for Jenkins to start... (Attempt $ATTEMPTS/$MAX_ATTEMPTS)"
  sleep 10
done

if [ $ATTEMPTS -eq $MAX_ATTEMPTS ]; then
  echo "Warning: Jenkins may not be fully started yet. Check logs with: docker-compose logs jenkins"
fi

# Display Jenkins URL and initial admin password
echo ""
echo "Jenkins is available at http://localhost:8080/"
echo "Username: admin"
echo "Password: admin"
echo ""
echo "Jenkins logs can be viewed with: docker-compose logs -f jenkins"
echo ""
echo "Container status:"
docker-compose ps
