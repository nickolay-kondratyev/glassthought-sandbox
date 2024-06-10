main() {
  eai intellij.silently_open "${GT_SANDBOX:?}"/bash/main.sh
}

main "${@}" || exit 1
