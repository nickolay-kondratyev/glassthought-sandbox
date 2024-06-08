main() {
  eai intellij.silently_open "${GT_SANDBOX:?}"/app/src/main/kotlin/gt/kotlin/sandbox/App.kt
}

main "${@}" || exit 1
