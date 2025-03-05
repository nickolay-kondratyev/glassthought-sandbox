main() {
  ./stop-jenkins.sh || throw "Failed to stop Jenkins"
  ./start-jenkins.sh || throw "Failed to start Jenkins"
}

main "${@}" || exit 1
