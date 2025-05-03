main() {
  chrome http://localhost:5173/
}

main "${@}" || exit 1
