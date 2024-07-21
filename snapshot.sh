main() {
  gt.snapshot "echo 'empty-run presuming the app was ran in Android Studio prior to snapshot'"
}

main "${@}" || exit 1
