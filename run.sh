main() {
  echo.yellow "No-OP."
  echo.green "Run the app within Android Studio, refer to ./doc/how-to-run.md"
}

main "${@}" || exit 1
