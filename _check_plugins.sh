include "./_env_setup_source_me.sh"


main() {
  echo "Checking plugins with CURL:"
  curl -u admin:"$(./_password.sh)" -s http://localhost:8080/pluginManager/api/json

  echo ""
  echo ""
  # shellcheck disable=SC2012
  echo "There are [$(ls "${JENKINS_HOME:?}"/plugins | wc -l)] plugins in [${JENKINS_HOME:?}/plugins] directory"
}

main "${@}" || exit 1
