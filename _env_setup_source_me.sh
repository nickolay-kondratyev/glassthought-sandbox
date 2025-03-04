# TODO: modify
export JENKINS_HOME="/Users/nickolaykondratyev/git_repos/glassthought-sandbox/out/.jenkins"
export JENKINS_ENVIRONMENT="$JENKINS_HOME/jenkins.environment"
# Determine Homebrew prefix based on architecture
export BREW_PREFIX
BREW_PREFIX=$(brew --prefix)
export JENKINS_WAR=${BREW_PREFIX:?}/opt/jenkins-lts/libexec/jenkins.war
export JENKINS_CLI_JAR
[[ -z "${JENKINS_CLI_JAR}" ]] && JENKINS_CLI_JAR="$(brew list jenkins-lts | grep '\.jar$')"
export JENKINS_PLIST_PATH="${BREW_PREFIX:?}/opt/jenkins-lts/homebrew.mxcl.jenkins-lts.plist"
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
