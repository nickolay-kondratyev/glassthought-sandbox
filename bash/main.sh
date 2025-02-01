SLEEP_DELAY_THAT_WE_WANT_TO_THROW_ON=5
START_MILLIS=$(date +%s%3N)

echo_log() {
  local millis_elapsed_since_start=$(( $(date +%s%3N) - START_MILLIS ))

  echo_dim "[$BASHPID][elapsed: ${millis_elapsed_since_start:?}] ${*}"
}
export -f echo_log

###############################################################################
# simulate_process
# Simulates a long-running process.
#
# Arguments:
#   $1 - Duration to sleep (in seconds)
#   $2 - Exit code to simulate (0 for success, non-zero for failure)
#
# Exits:
#   Exits with the provided exit code.
###############################################################################
simulate_process() {

  local seconds_to_sleep="${1:?seconds_to_sleep}"
  echo_log "simulate_process that will sleep for ${seconds_to_sleep:?} seconds, and exit with [${2}]: \$\$ = $$ (parent), \$BASHPID = $BASHPID (child)"

  sleep "${seconds_to_sleep:?}"
  if [[ "${seconds_to_sleep:?}" == "${SLEEP_DELAY_THAT_WE_WANT_TO_THROW_ON:?}" ]]; then
      echo_log "Enough of sleeping, let's throw an error"
      throw "throw on sleep of ${SLEEP_DELAY_THAT_WE_WANT_TO_THROW_ON:?} on purpose"
  fi
  exit "$2"  # Simulates success (0) or failure (non-zero)
}


###############################################################################
# wait_for_processes
# Waits for all background processes specified by their PIDs.
#
# Example code:
# ```
#  # Array to store process PIDs
#  pids=()
#
#  # Start multiple background processes with different durations and exit codes
#  simulate_process 1 0 & pids+=("$!")  # Simulates success
#  simulate_process 2 0 & pids+=("$!")  # Simulates success
#  simulate_process 3 1 & pids+=("$!")  # Simulates success
#
#  # Wait for all processes and check the overall exit status
#  if wait_for_processes "${pids[@]}"; then
#    echo.green "All processes completed successfully."
#  else
#    echo.red "Error: One or more processes failed." >&2
#    exit 1
#  fi
# ```
# --------------------------------------------------------------------------------
# Arguments:
#   List of PIDs to wait for.
#
# Returns:
#   0 if all processes exit successfully, 1 if any process fails.
#
# Side Effects:
#   Prints the exit status of each process.
#
###############################################################################
wait_for_processes() {
    local pids=("$@")
    local exit_status=0

    for pid in "${pids[@]}"; do
        echo_log "Waiting for process $pid to complete"
        # Wait for the process to complete
        wait "$pid"
        local code=$?
        echo_log "Process $pid finished with exit code $code"

        # If any process fails, mark overall status as failure
        if [[ $code -ne 0 ]]; then
            exit_status=1
        fi
    done

    return $exit_status
}

main() {
  echo_log "Starting main script: \$\$ = $$, \$BASHPID = $BASHPID"
  # Array to store process PIDs
  pids=()

  # Start multiple background processes with different durations and exit codes
  simulate_process 1 0 & pids+=("$!")  # Simulates success
  simulate_process 2 0 & pids+=("$!")  # Simulates success
  simulate_process 3 1 & pids+=("$!")  # Simulates success
  # simulate_process ${SLEEP_DELAY_THAT_WE_WANT_TO_THROW_ON:?} 0 & pids+=("$!")  # Simulates failure

  # Wait for all processes and check the overall exit status
  if wait_for_processes "${pids[@]}"; then
    echo.green "All processes completed successfully."
  else
    echo.red "Error: One or more processes failed." >&2
    exit 1
  fi
}

main "${@}" || exit 1
