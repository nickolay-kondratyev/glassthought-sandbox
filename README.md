# Jenkins in Docker for THORG

This repository contains the necessary configuration to set up Jenkins in Docker for the THORG project. The setup is designed to monitor the THORG Git repository and run build scripts when changes are detected.

## Prerequisites

- Docker and Docker Compose installed
  - Docker version 20.10.0 or newer
  - Docker Compose version 1.29.0 or newer (both V1 and V2 are supported)
  - The setup script will automatically verify these prerequisites
  - If not installed or outdated, please follow the official installation guides:
    - [Docker Installation Guide](https://docs.docker.com/get-docker/)
    - [Docker Compose Installation Guide](https://docs.docker.com/compose/install/)
- SSH key with access to the THORG Git repository (`git@gitlab.com:thorg/thorg-root.git`)

## Quick Start

1. Clone this repository:
   ```bash
   git clone <this-repository-url>
   cd <repository-directory>
   ```

2. Start Jenkins:
   ```bash
   ./start-jenkins.sh
   ```

   The script will:
   - Verify that Docker and Docker Compose are installed and meet minimum version requirements
   - Check that Docker Compose can support the compose file version used (3.8)
   - Check that your SSH key exists
   - Build and start the Jenkins container
   - Wait for Jenkins to be fully started and ready to use

   By default, the script will use your SSH key at `~/.ssh/id_rsa`. If your SSH key is in a different location, you can specify it:
   ```bash
   SSH_KEY_PATH=/path/to/your/ssh/key ./start-jenkins.sh
   ```

3. Access Jenkins at http://localhost:8080/
   - Username: `admin`
   - Password: `admin`

4. Stop Jenkins:
   ```bash
   ./stop-jenkins.sh
   ```

## Configuration Details

### Docker Setup

The setup consists of:
- `Dockerfile`: Builds a Jenkins image with necessary tools (Git, Bash 5.x+, Gradle, etc.)
- `docker-compose.yml`: Configures the Jenkins container with proper volumes and environment variables
- `plugins.txt`: Lists the Jenkins plugins to be installed
- `jenkins-casc.yaml`: Jenkins Configuration as Code for automatic setup
- `init.groovy.d/`: Contains initialization scripts for Jenkins
- `verify-prerequisites.sh`: Script to verify that Docker and Docker Compose are installed and compatible
- `start-jenkins.sh`: Script to start Jenkins with proper configuration
- `stop-jenkins.sh`: Script to stop Jenkins

### Jenkins Pipeline

A sample Jenkinsfile (`Jenkinsfile.sample`) is provided that:
1. Checks out the THORG repository
2. Runs the initialization script (`init.sh`)
3. Sources the THORG Bash environment (`shell/env/thorg-env.sh`)
4. Runs the sanity check script (`sanity_check.sh`)
5. Handles errors and reports build status

### SSH Key Configuration

The SSH key is mounted into the Jenkins container to allow access to the THORG Git repository. The key is mounted at `/var/jenkins_home/.ssh/id_rsa` and proper permissions are set automatically.

## Customizing the Setup

### Modifying Jenkins Configuration

The Jenkins Configuration as Code file (`jenkins-casc.yaml`) can be modified to:
- Change the admin credentials
- Add more users
- Configure additional jobs
- Set up additional credentials

### Customizing the Jenkinsfile

The sample Jenkinsfile can be customized to:
- Add more build stages
- Run additional tests
- Deploy artifacts
- Send notifications

### Adding More Plugins

To add more Jenkins plugins, edit the `plugins.txt` file and add the plugin IDs.

## Troubleshooting

### Prerequisites Issues

If the prerequisites check fails:
1. Ensure Docker is installed (version 20.10.0 or newer) and the Docker daemon is running
2. Ensure Docker Compose is installed (version 1.29.0 or newer)
   - Both Docker Compose V1 (`docker-compose`) and V2 (`docker compose`) are supported
   - If using Docker Compose V1, version 1.27.0+ is required to support Compose file version 3.8
   - If using Docker Compose V2, version 2.0.0+ is required
3. Check the error message for specific instructions on how to upgrade

### Build Issues

If the Docker build fails:
1. Check the build logs for specific errors
2. Common issues include:
   - Network connectivity problems when downloading packages
   - Repository or package availability issues
   - Incompatible package versions
3. If you see errors related to Docker CLI installation:
   - The script uses the official Docker installation method
   - You can try rebuilding with: `docker-compose build --no-cache jenkins`
   - Check Docker Hub for any known issues with the Jenkins base image

### Container Startup Issues

If the Jenkins container fails to start or is not accessible:
1. Check the container status: `docker-compose ps`
2. View the container logs: `docker-compose logs jenkins`
3. Ensure ports 8080 and 50000 are not already in use by other services
4. Verify that the Docker daemon has sufficient resources (CPU, memory, disk space)
5. Try restarting the container: `docker-compose restart jenkins`

### SSH Key Issues

If Jenkins cannot access the Git repository:
1. Verify that your SSH key has access to the repository
2. Check that the SSH key is correctly mounted in the container
3. Verify SSH key permissions (should be 600)

### Build Failures

If the build fails:
1. Check the Jenkins logs for errors
2. Verify that the THORG scripts (`init.sh`, `sanity_check.sh`) are executable
3. Ensure the Bash environment is correctly sourced

## Maintenance

### Updating Jenkins

To update Jenkins to a newer version:
1. Update the base image version in the `Dockerfile`
2. Rebuild the container: `docker-compose build --no-cache`
3. Restart Jenkins: `docker-compose up -d`

### Backing Up Jenkins

The Jenkins data is stored in a Docker volume. To back it up:
1. Stop Jenkins: `./stop-jenkins.sh`
2. Backup the volume: `docker volume create --name jenkins_backup && docker run --rm -v jenkins_home:/source -v jenkins_backup:/destination alpine cp -a /source/. /destination/`
3. Start Jenkins: `./start-jenkins.sh`

### Cleaning Up

If you need to completely reset the Jenkins setup:
1. Stop Jenkins: `./stop-jenkins.sh`
2. Remove the volume: `docker volume rm jenkins_home`
3. Start Jenkins again: `./start-jenkins.sh`
