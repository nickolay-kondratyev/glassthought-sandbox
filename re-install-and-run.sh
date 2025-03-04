main() {
  eai ./uninstall-jenkins.sh
  eai ./install-jenkins.sh
  eai ./start-jenkins.sh
  eai ./install-plugins.sh
}

main "${@}" || exit 1
