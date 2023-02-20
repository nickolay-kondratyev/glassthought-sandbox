set -e

failingFunc() {
  echo "failingFunc will return 1"
  return 1
}

failing-main_MAIN() {
  echo "PrintA, will 'return 1', on next line."

  failingFunc

  echo "PrintB, will should NOT be printed. (after 'return 1')"
}
