#!/usr/bin/env bash

# Function to check if a command exists
command_exists() {
  command -v "$1" >/dev/null 2>&1
}

# Function to compare versions
version_gt() {
  test "$(printf '%s\n' "$1" "$2" | sort -V | head -n 1)" != "$1"
}

# Check if Docker is installed
if ! command_exists docker; then
  echo "Error: Docker is not installed or not in PATH"
  echo "Please install Docker: https://docs.docker.com/get-docker/"
  throw "Docker is not installed"
  exit 1
fi


# Check Docker version
DOCKER_VERSION=$(docker --version | awk '{print $3}' | tr -d ',')
echo "Docker version: $DOCKER_VERSION"

# Minimum required Docker version
MIN_DOCKER_VERSION="20.10.0"
if version_gt "$MIN_DOCKER_VERSION" "$DOCKER_VERSION"; then
  echo "Error: Docker version $DOCKER_VERSION is too old"
  echo "Please upgrade Docker to version $MIN_DOCKER_VERSION or newer"
  throw "Docker version $DOCKER_VERSION is too old (minimum required: $MIN_DOCKER_VERSION)"
  exit 1
fi

# Check if Docker Compose is installed
# First try docker compose (V2)
if command_exists "docker" && docker compose version >/dev/null 2>&1; then
  COMPOSE_VERSION=$(docker compose version --short)
  COMPOSE_V2=true
  echo "Docker Compose V2 version: $COMPOSE_VERSION"
# Then try docker-compose (V1)
elif command_exists docker-compose; then
  COMPOSE_VERSION=$(docker-compose --version | awk '{print $3}' | tr -d ',')
  COMPOSE_V2=false
  echo "Docker Compose V1 version: $COMPOSE_VERSION"
else
  echo "Error: Docker Compose is not installed or not in PATH"
  echo "Please install Docker Compose: https://docs.docker.com/compose/install/"
  throw "Docker Compose is not installed"
  exit 1
fi

# Minimum required Docker Compose version
MIN_COMPOSE_VERSION="1.29.0"
if version_gt "$MIN_COMPOSE_VERSION" "$COMPOSE_VERSION"; then
  echo "Error: Docker Compose version $COMPOSE_VERSION is too old"
  echo "Please upgrade Docker Compose to version $MIN_COMPOSE_VERSION or newer"
  throw "Docker Compose version $COMPOSE_VERSION is too old (minimum required: $MIN_COMPOSE_VERSION)"
  exit 1
fi

# Check if Docker daemon is running
if ! docker info >/dev/null 2>&1; then
  echo "Error: Docker daemon is not running"
  echo "Please start Docker daemon"
  throw "Docker daemon is not running"
  exit 1
fi

# Check if Docker Compose file version is supported
if [ "$COMPOSE_V2" = true ]; then
  # For Docker Compose V2
  COMPOSE_FILE_VERSION=$(grep -m 1 "^version:" docker-compose.yml | awk '{print $2}' | tr -d "'\"")
  if [ -z "$COMPOSE_FILE_VERSION" ]; then
    echo "Warning: Could not determine Docker Compose file version"
  else
    echo "Docker Compose file version: $COMPOSE_FILE_VERSION"
    # Check if the compose file version is supported by the installed Docker Compose
    if [ "$COMPOSE_FILE_VERSION" = "3.8" ] && version_gt "2.0.0" "$COMPOSE_VERSION"; then
      echo "Error: Docker Compose version $COMPOSE_VERSION does not support Compose file version $COMPOSE_FILE_VERSION"
      echo "Please upgrade Docker Compose to version 2.0.0 or newer"
      throw "Docker Compose version $COMPOSE_VERSION does not support Compose file version $COMPOSE_FILE_VERSION"
      exit 1
    fi
  fi
else
  # For Docker Compose V1
  COMPOSE_FILE_VERSION=$(grep -m 1 "^version:" docker-compose.yml | awk '{print $2}' | tr -d "'\"")
  if [ -z "$COMPOSE_FILE_VERSION" ]; then
    echo "Warning: Could not determine Docker Compose file version"
  else
    echo "Docker Compose file version: $COMPOSE_FILE_VERSION"
    # Check if the compose file version is supported by the installed Docker Compose
    if [ "$COMPOSE_FILE_VERSION" = "3.8" ] && version_gt "1.27.0" "$COMPOSE_VERSION"; then
      echo "Error: Docker Compose version $COMPOSE_VERSION does not support Compose file version $COMPOSE_FILE_VERSION"
      echo "Please upgrade Docker Compose to version 1.27.0 or newer"
      throw "Docker Compose version $COMPOSE_VERSION does not support Compose file version $COMPOSE_FILE_VERSION"
      exit 1
    fi
  fi
fi

echo "All prerequisites are satisfied!"
