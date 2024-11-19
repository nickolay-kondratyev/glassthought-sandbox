main() {
  export GT_SNAPSHOT_OPTIONAL_CODE_FILE_TO_INCLUDE_IN_MSG="${GT_SANDBOX_REPO:?}/app/src/test/kotlin/com/glassthought/sandbox/MainTest.kt"
  gt.snapshot "./gradlew test --rerun-tasks"
}

main "${@}" || exit 1
