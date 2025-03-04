source "_env_setup_source_me.sh"
main() {
  ./uninstall-jenkins.sh || throw "Failed to uninstall Jenkins"
  ./install-jenkins.sh || throw "Failed to install Jenkins"
  ./start-jenkins.sh || throw "Failed to start Jenkins"
  ./install-plugins.sh || throw "Failed to install plugins"
  ./stop-jenkins.sh || throw "Failed to stop Jenkins"
  ./start-jenkins.sh || throw "Failed to start Jenkins"
}

main "${@}" || exit 1
