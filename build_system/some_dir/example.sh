example.MAIN() {
  echo "GT_SANDBOX_REPO=$GT_SANDBOX_REPO"
  echo "Hello from BASH named function ${FUNCNAME[0]} that uses periods."
}
export -f example.MAIN

example.MAIN || exit 1
