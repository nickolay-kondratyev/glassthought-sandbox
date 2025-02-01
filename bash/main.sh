#!/bin/bash

# Function to simulate a long-running process
simulate_process() {
    sleep "$1"
    exit "$2"  # Simulates success (0) or failure (non-zero)
}

# Array to store process PIDs
pids=()

# Start multiple background processes with different durations and exit codes
simulate_process 2 0 & pids+=($!)  # Simulates success
simulate_process 3 1 & pids+=($!)  # Simulates failure
simulate_process 1 0 & pids+=($!)  # Simulates success

# Track overall success/failure
exit_status=0

# Wait for all processes and capture their exit codes
for pid in "${pids[@]}"; do
    wait "$pid"
    code=$?
    echo "Process $pid finished with exit code $code"

    if [[ $code -ne 0 ]]; then
        exit_status=1  # Mark as failure if any process fails
    fi
done

# Final exit based on process results
if [[ $exit_status -ne 0 ]]; then
    echo "Error: One or more processes failed." >&2
    exit 1
fi

echo "All processes completed successfully."
exit 0
