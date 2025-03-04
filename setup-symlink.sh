main() {
  ln -s $HOME/.jenkins ./out/.jenkins
}

main "${@}" || exit 1
