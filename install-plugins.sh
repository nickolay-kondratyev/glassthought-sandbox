#!/usr/bin/env bash
# shellcheck disable=SC2002

set -e

source "_env_setup_source_me.sh"

echo "--------------------------------------------------------------------------------"
echo -e "${GREEN:?}Starting Jenkins plugin installation...${NC:?}"

export ADMIN_PASSWORD=$(./_password.sh)

_install_plugin(){
  local plugin="${1:?plugin}"

  echo.log "Installing plugin=[$plugin]"

  if [[ ! -f "${JENKINS_CLI_JAR}" ]]
  then
  	throw "Jenkins CLI jar not found at path=[$JENKINS_CLI_JAR]"
  fi


  java -jar "${JENKINS_CLI_JAR:?}" -s "${JENKINS_URL:?}" \
    -auth admin:"${ADMIN_PASSWORD:?}" \
    install-plugin "${plugin:?}" || {
      throw "Failed to install plugin=[$plugin]"
    } && echo.log "Finished installing plugin=[$plugin]"
}

echo.log "Installing plugins using Jenkins CLI"
mapfile -t plugins < plugins.txt
for plugin in "${plugins[@]}"; do
  # -n: return true when value is not empty.
  if [[ -n "${plugin}" ]]; then
    _install_plugin "$plugin"
  fi
done
echo.log "Finished installing plugins"

echo.log "Restarting Jenkins"
java -jar "${JENKINS_CLI_JAR:?}" -s "${JENKINS_URL:?}" -auth admin:"${ADMIN_PASSWORD:?}" safe-restart

# Wait for Jenkins to come back up
echo.log "Waiting for Jenkins to restart..."
sleep 30  # Initial wait for shutdown

ATTEMPTS=0
MAX_ATTEMPTS=30
while [ $ATTEMPTS -lt $MAX_ATTEMPTS ]; do
    if curl -s -f "${JENKINS_URL}/login" > /dev/null; then
        echo.log "Jenkins is back up!"
        break
    fi
    ATTEMPTS=$((ATTEMPTS+1))
    echo.log "Waiting for Jenkins to start... (Attempt $ATTEMPTS/$MAX_ATTEMPTS)"
    sleep 10
done

if [ $ATTEMPTS -eq $MAX_ATTEMPTS ]; then
    throw "Jenkins failed to restart within the expected time"
fi

# Execute Groovy scripts after plugins are installed and Jenkins is back up
echo.log "Executing Groovy configurations..."
for script in init.groovy.d/*.groovy; do
    if [ -f "$script" ]; then
        echo.log "Executing $script..."
        java -jar "${JENKINS_CLI_JAR}" -s "${JENKINS_URL}" -auth admin:"${ADMIN_PASSWORD}" groovy = < "$script" || throw "Failed to execute script=[$script]"
    fi
done
echo.log "Groovy configurations completed"
