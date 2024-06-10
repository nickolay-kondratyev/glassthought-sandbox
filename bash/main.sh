main() {
  echo hi
}

main "${@}" || exit 1
