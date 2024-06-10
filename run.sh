run_main() {
  ei cd ./bash

  ./main.sh
}

run_main "${@}" || exit 1
