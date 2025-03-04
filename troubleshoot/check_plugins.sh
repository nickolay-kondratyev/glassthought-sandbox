main() {
  curl -u admin:"$(./password.sh)" -s http://localhost:8080/pluginManager/api/json
}

main "${@}" || exit 1
