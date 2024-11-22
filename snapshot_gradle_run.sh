main() {
  gt.snapshot "./gradlew run --quite"
}

main "${@}" || exit 1
