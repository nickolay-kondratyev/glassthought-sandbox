set -e

func-which-returns-1() {
  echo "I will return 1"
  return 1
}
export -f func-which-returns-1

set-e-demo_MAIN() {
  func-which-returns-1

  echo "This line will not be printed."
}

set-e-demo_MAIN
