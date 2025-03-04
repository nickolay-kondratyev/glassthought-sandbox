include "_env_variables_source_me.sh"

main() {
  echo ""
  echo "Username: admin"
  echo "Initial admin password: $(cat "${JENKINS_HOME:?}/secrets/initialAdminPassword")"
}

main "${@}" || exit 1
