# Define a sample function for testing the behavior of PIPESTATUS
sample_command_that_fails() {
  echo ""
  echo 'Running Sample Command that fails (returns 1)...'
  return 1
}

# Temporary file for output capture
OUTPUT_FILE="/tmp/sample_output.txt"

correct(){
  sample_command_that_fails | tee "${OUTPUT_FILE:?}"
  echo.green "Pipe status captured as expected: ${PIPESTATUS[0]}"
}


incorrect(){
    sample_command_that_fails | tee "${OUTPUT_FILE:?}"
    SOME_VAR="${OUTPUT_FILE:?}"
    echo.yellow "Pipe status got reset without a PIPE call: ${PIPESTATUS[0]}"
}

main() {
  correct
  incorrect
}

main "${@}" || exit 1
