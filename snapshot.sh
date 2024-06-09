main() {
  gt.snapshot "./gradlew run"
}

main "${@}" || exit 1
