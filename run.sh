main() {
  ei cd ./bash

  ./main.sh
}

main "${@}" || exit 1
