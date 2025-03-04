main() {
  mkdir -p ./out
  ln -s $HOME/.jenkins ./out/.jenkins
}

main "${@}" || exit 1
