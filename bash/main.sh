set -e

func-which-returns-1() {
  echo.log "func-which-returns-1: I will return 1"

  return 1
}

set-e-demo_MAIN() {
  func-which-returns-1 || {
    echo.log "func-which-returns-1 returned non-zero exit code."
  }

  echo.log "IF we dont handle return code of 'func-which-returns-1' this will not be printed."
}

set-e-demo_MAIN
