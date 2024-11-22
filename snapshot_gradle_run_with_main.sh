main() {
  export GT_SNAPSHOT_OPTIONAL_CODE_FILE_TO_INCLUDE_IN_MSG=${GT_SANDBOX_REPO:?}/app/src/main/kotlin/com/glassthought/sandbox/Main.kt
  gt.snapshot "./gradlew run --quite"
}

main "${@}" || exit 1
