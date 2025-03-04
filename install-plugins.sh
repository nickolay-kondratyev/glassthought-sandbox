#!/usr/bin/env bash

set -e

source "_env_setup_source_me.sh"

JENKINS_URL=http://localhost:8080
ADMIN_PASSWORD=$(./_password.sh)

echo.log "Password: ${ADMIN_PASSWORD:?}"

echo.log "Installing plugins using Jenkins CLI"
while read -r plugin; do
  echo.log "Installing plugin: $plugin"
  java -jar "${JENKINS_CLI_JAR:?}" -s $JENKINS_URL \
    -auth admin:"$ADMIN_PASSWORD" \
    install-plugin "$plugin"
done < plugins.txt
echo.log "Finished installing plugins"

echo.log "Restarting Jenkins"
java -jar "${JENKINS_CLI_JAR:?}" -s $JENKINS_URL \
  -auth admin:"$ADMIN_PASSWORD" \
  safe-restart
