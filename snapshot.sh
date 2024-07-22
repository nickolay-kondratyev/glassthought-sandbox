main() {
  # -z: returns true when value is empty.
  if [[ -z "${1}" ]]; then
    gt.snapshot "./run.sh"
  else
    gt.snapshot.with_custom_message "${1}" "./run.sh"
  fi
}

main "${@}" || exit 1
