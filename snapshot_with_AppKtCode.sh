main() {
  export GT_SNAPSHOT_OPTIONAL_CODE_FILE_TO_INCLUDE_IN_MSG="${GT_SANDBOX:?}/app/src/main/kotlin/gt/sandbox/App.kt"
  gt.snapshot "./gradlew run"
}

main "${@}" || exit 1
