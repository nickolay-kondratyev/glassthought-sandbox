main() {
  eai npm install --global yarn
  eai yarn install
  eai yarn run dev
}

main "${@}" || exit 1
