main() {
  ./gradlew runMainQuietly
}

main "${@}" || exit 1
