# Native Jenkins Installation for macOS

This repository contains scripts to install, configure, and manage Jenkins natively on macOS without Docker.

## Prerequisites

- macOS operating system
- Administrative privileges (for some installation steps)
- SSH key for GitLab access (default: `~/.ssh/id_rsa`)

## Installation

1. Clone this repository:
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. Make the scripts executable:
   ```bash
   chmod +x *.sh
   ```

3. Verify prerequisites:
   ```bash
   ./verify-prerequisites.sh
   ```

4. Install Jenkins:
   ```bash
   ./install-jenkins.sh
   ```

5. Start Jenkins:
   ```bash
   ./start-jenkins.sh
   ```

6. Install plugins (after Jenkins has started):
   ```bash
   ./install-plugins.sh
   ```

## Usage

### Starting Jenkins

```bash
./start-jenkins.sh
```

Jenkins will be available at http://localhost:8080/

Default credentials:
- Username: `admin`
- Password: `admin`

### Stopping Jenkins

```bash
./stop-jenkins.sh
```

### Updating Jenkins

```bash
./update-jenkins.sh
```

### Uninstalling Jenkins

```bash
./uninstall-jenkins.sh
```

## Configuration

Jenkins is configured using the following files:

- `jenkins-casc.yaml`: Configuration as Code for Jenkins
- `plugins.txt`: List of plugins to install
- `init.groovy.d/`: Groovy scripts for Jenkins initialization

## Environment Variables

You can customize the installation by setting the following environment variables:

- `SSH_KEY_PATH`: Path to the SSH key for GitLab access (default: `~/.ssh/id_rsa`)

Example:
```bash
SSH_KEY_PATH=~/.ssh/my_custom_key ./install-jenkins.sh
```

## Troubleshooting

### Viewing Jenkins Logs

```bash
brew services log jenkins-lts
```

### Common Issues

1. **Jenkins fails to start**
   - Check if Java is installed correctly: `java -version`
   - Ensure you have sufficient permissions for the Jenkins home directory

2. **Plugin installation fails**
   - Ensure Jenkins is running before installing plugins
   - Check your internet connection
   - Try installing plugins manually through the Jenkins UI

3. **SSH key issues**
   - Verify your SSH key exists and has the correct permissions
   - Ensure the SSH key is properly configured for GitLab access

## Directory Structure

- `~/.jenkins/`: Jenkins home directory
  - `casc_configs/`: Configuration as Code files
  - `init.groovy.d/`: Initialization scripts
  - `.ssh/`: SSH keys for Jenkins
  - `plugins/`: Installed plugins
  - `workspace/`: Jenkins job workspaces

## Maintenance

### Backing Up Jenkins

The Jenkins home directory (`~/.jenkins`) contains all configuration and job data. To back up Jenkins:

```bash
cp -r ~/.jenkins ~/jenkins_backup_$(date +%Y%m%d)
```

### Restoring from Backup

```bash
./stop-jenkins.sh
rm -rf ~/.jenkins
cp -r ~/jenkins_backup_YYYYMMDD ~/.jenkins
./start-jenkins.sh
``` 