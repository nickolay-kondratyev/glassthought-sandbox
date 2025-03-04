source "../_env_setup_source_me.sh"


main() {
  echo "With CURL:"
  curl -u admin:"$(./password.sh)" -s http://localhost:8080/pluginManager/api/json

  echo "With ls:"
  ls -la "${JENKINS_HOME:?}"/plugins
}

main "${@}" || exit 1
