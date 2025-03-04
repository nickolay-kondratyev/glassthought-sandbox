# TODO: modify
export JENKINS_HOME="/Users/nickolaykondratyev/git_repos/glassthought-sandbox/out/.jenkins"
export JENKINS_ENVIRONMENT="$JENKINS_HOME/jenkins.environment"
export JENKINS_WAR=$(brew --prefix)/opt/jenkins-lts/libexec/jenkins.war
# -z: returns true when value is empty.
if [[ -z "${JENKINS_CLI_JAR}" ]]; then
  export JENKINS_CLI_JAR="$(brew list jenkins-lts | grep '\.jar$')"
fi



export JENKINS_URL="http://localhost:8080"
export ADMIN_USER="admin"

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
