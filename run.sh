main() {
  echo.yellow "No-OP."
  echo.green "Run the app within Android Studio. Refer to https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-create-first-app.html#run-your-application-on-android"
}

main "${@}" || exit 1
