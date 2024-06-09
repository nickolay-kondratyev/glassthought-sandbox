main() {
  gt.snapshot "./gradlew runMainQuietly"
}

main "${@}" || exit 1
