calls-failing-main_MAIN() {
  source ./failing-main.sh

  failing-main_MAIN || return 1

  echo "PrintC, will should NOT be printed. (after 'return 1')"
}

calls-failing-main_MAIN
