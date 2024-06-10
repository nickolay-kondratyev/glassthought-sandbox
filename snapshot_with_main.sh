main() {
  ei cd ./bash

  export GT_SNAPSHOT_OPTIONAL_CODE_FILE_TO_INCLUDE_IN_MSG="${GT_SANDBOX:?}/bash/main.sh"
  gt.snapshot "./main.sh"
}

main "${@}" || exit 1
