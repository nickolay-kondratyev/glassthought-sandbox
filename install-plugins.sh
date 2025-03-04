#!/usr/bin/env bash

set -e

source "_env_setup_source_me.sh"

export ADMIN_PASSWORD=$(./_password.sh)

_install_plugin(){
  local plugin="${1:?plugin}"

  java -jar "${JENKINS_CLI_JAR:?}" -s "${JENKINS_URL:?}" \
    -auth admin:"${ADMIN_PASSWORD:?}" \
    install-plugin "${plugin:?}"
}


echo.log "Password: ${ADMIN_PASSWORD:?}"

echo.log "Installing plugins using Jenkins CLI"



cat plugins.txt | while IFS= read -r plugin; do
  echo.log "Installing plugin: $plugin"

  _install_plugin "$plugin"
done
echo.log "Finished installing plugins"

echo.log "Restarting Jenkins"
java -jar "${JENKINS_CLI_JAR:?}" -s $JENKINS_URL \
  -auth admin:"${ADMIN_PASSWORD:?}" \
  safe-restart
