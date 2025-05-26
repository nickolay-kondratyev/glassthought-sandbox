main() {
  gt.snapshot "chrome index.html"
}

main "${@}" || exit 1
