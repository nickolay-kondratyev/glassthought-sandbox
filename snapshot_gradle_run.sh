main() {
  gt.snapshot "./gradlew run --quiet"
}

main "${@}" || exit 1
