main() {
  gt.snapshot "gradle run"
}

main "${@}" || exit 1
