FROM jenkins/jenkins:lts-jdk17

USER root

# Install necessary tools
RUN apt-get update && \
    apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release \
    git \
    bash \
    openssh-client \
    gradle \
    && rm -rf /var/lib/apt/lists/*

# Install Docker CLI using the recommended approach for the Jenkins image
RUN curl -fsSL https://get.docker.com -o get-docker.sh && \
    sh get-docker.sh && \
    apt-get install -y docker-ce-cli && \
    rm -rf /var/lib/apt/lists/* && \
    rm get-docker.sh

# Install Bash 5.x+
RUN apt-get update && \
    apt-get install -y bash && \
    rm -rf /var/lib/apt/lists/*

# Switch back to jenkins user
USER jenkins

# Skip initial setup
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

# Install Jenkins plugins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

# Copy Jenkins configuration
COPY jenkins-casc.yaml /var/jenkins_home/casc_configs/jenkins.yaml
ENV CASC_JENKINS_CONFIG=/var/jenkins_home/casc_configs/jenkins.yaml

# Copy init scripts
COPY init.groovy.d/ /usr/share/jenkins/ref/init.groovy.d/

# Set up environment variables
ENV THORG_ROOT=/var/jenkins_home/workspace/thorg-root 