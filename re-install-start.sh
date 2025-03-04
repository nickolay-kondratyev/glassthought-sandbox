source "_env_setup_source_me.sh"
main() {
  ./uninstall-jenkins.sh || throw "Failed to uninstall Jenkins"
  ./install-jenkins.sh || throw "Failed to install Jenkins"

  # run configure-jenkins-home.sh with sudo in a subshell
  (sudo ./_configure-jenkins-home.sh || throw "Failed to configure Jenkins home")

  ./start-jenkins.sh || throw "Failed to start Jenkins"
  ./install-plugins.sh || throw "Failed to install plugins"
  ./stop-jenkins.sh || throw "Failed to stop Jenkins"
  ./start-jenkins.sh || throw "Failed to start Jenkins"
}

main "${@}" || exit 1
