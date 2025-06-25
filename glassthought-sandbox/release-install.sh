main() {
  eai npm install
  eai npm install -g vsce
  eai npm run compile

  # Package the extension.
  yes | vsce package || {
    throw "Failed to package the extension."
  }

  eai ./_install.sh
}

main "${@}" || exit 1
