main() {
  gt.snapshot "chrome ./main/index.html"
}

main "${@}" || exit 1
