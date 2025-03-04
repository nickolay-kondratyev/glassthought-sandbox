#!/usr/bin/env bash
# shellcheck disable=SC2002

set -e

source "_env_setup_source_me.sh"

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
      echo.red "Failed to install plugin=[$plugin]"
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
