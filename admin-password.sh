main() {
  echo ""
  echo "Username: admin"
  echo "Initial admin password: $(cat "${HOME:?}"/.jenkins/secrets/initialAdminPassword)"
}

main "${@}" || exit 1
