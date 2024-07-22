main() {
  gt.snapshot "./run.sh"
}

main "${@}" || exit 1
