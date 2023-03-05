example_MAIN() {
  echo "Hello from shell script."
}
export -f example_MAIN

example_MAIN || exit 1
