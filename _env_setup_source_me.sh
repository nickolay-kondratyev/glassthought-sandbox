# TODO: modify
export JENKINS_HOME="/Users/nickolaykondratyev/git_repos/glassthought-sandbox/out/.jenkins"
export JENKINS_ENVIRONMENT="$JENKINS_HOME/jenkins.environment"
export JENKINS_WAR=$(brew --prefix)/opt/jenkins-lts/libexec/jenkins.war
export JENKINS_CLI_JAR="${JENKINS_HOME:?}/jenkins-cli.jar"
export JENKINS_URL="http://localhost:8080"
export ADMIN_USER="admin"

# Note this might not actually take effect right now and contents of
# ${JENKINS_HOME:?}/secrets/initialAdminPassword file need to be read instead.
export ADMIN_PASSWORD="admin"

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color


# Need to redefine due to SUDO usage.
throw() {
  echo "Failed: ${*}"
  exit 1
}
export -f throw
