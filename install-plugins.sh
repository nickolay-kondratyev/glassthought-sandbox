#!/usr/bin/env bash
# shellcheck disable=SC2002

set -e

source "_env_setup_source_me.sh"

export ADMIN_PASSWORD=$(./_password.sh)

_install_plugin(){
  local plugin="${1:?plugin}"

  echo.log "Installing plugin=[$plugin]"

  java -jar "${JENKINS_CLI_JAR:?}" -s "${JENKINS_URL:?}" \
    -auth admin:"${ADMIN_PASSWORD:?}" \
    install-plugin "${plugin:?}" || {
      echo.red "Failed to install plugin=[$plugin]"
    }
}

echo.log "Installing plugins using Jenkins CLI"
mapfile -t plugins < plugins.txt
for plugin in "${plugins[@]}"; do
  _install_plugin "$plugin"
done
echo.log "Finished installing plugins"

echo.log "Restarting Jenkins"
java -jar "${JENKINS_CLI_JAR:?}" -s $JENKINS_URL \
  -auth admin:"${ADMIN_PASSWORD:?}" \
  safe-restart
