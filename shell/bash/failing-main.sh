set -e

failingFunc() {
  echo "failingFunc"
  return 1
}

failing-main_MAIN() {
  echo "PrintA, will 'return 1', on next line."

  failingFunc

  echo "PrintB, will should NOT be printed. (after 'return 1')"
}

failing-main_MAIN
