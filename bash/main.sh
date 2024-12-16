main() {
  echo "Start in main- BASHPID=$BASHPID"

  (echo "In parenthesis spawned subshell- BASHPID=$BASHPID")

  echo "End in main- BASHPID=$BASHPID"
}

main "${@}" || exit 1
