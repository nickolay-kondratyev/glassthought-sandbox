#!/bin/bash

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
    sleep "$1"
    exit "$2"  # Simulates success (0) or failure (non-zero)
}

###############################################################################
# wait_for_processes
# Waits for all background processes specified by their PIDs.
#
# Arguments:
#   List of PIDs to wait for.
#
# Returns:
#   0 if all processes exit successfully, 1 if any process fails.
#
# Side Effects:
#   Prints the exit status of each process.
###############################################################################
wait_for_processes() {
    local pids=("$@")
    local exit_status=0

    for pid in "${pids[@]}"; do
        # Wait for the process to complete
        wait "$pid"
        local code=$?
        echo "Process $pid finished with exit code $code"

        # If any process fails, mark overall status as failure
        if [[ $code -ne 0 ]]; then
            exit_status=1
        fi
    done

    return $exit_status
}

# Array to store process PIDs
pids=()

# Start multiple background processes with different durations and exit codes
simulate_process 2 0 & pids+=("$!")  # Simulates success
simulate_process 3 1 & pids+=("$!")  # Simulates failure
simulate_process 1 0 & pids+=("$!")  # Simulates success

# Wait for all processes and check the overall exit status
wait_for_processes "${pids[@]}"
result=$?

if [[ $result -ne 0 ]]; then
    echo "Error: One or more processes failed." >&2
    exit 1
fi

echo "All processes completed successfully."
exit 0
