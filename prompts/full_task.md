# Technical Prompt: Jenkins Integration for THORG

## Project Context: THORG
- Git repository: `git@gitlab.com:thorg/thorg-root.git`
   - `git@gitlab.com:thorg/thorg-root.git` will be `$THORG_ROOT`.

## Requirements
0. Minimize manual steps.
1. Set up Jenkins in Docker to run automated sanity checks on the THORG repository.
2. Set up `git@gitlab.com:thorg/thorg-root.git` as `$THORG_ROOT`:
   - Expect the SSH key on the box to have access to `git@gitlab.com:thorg/thorg-root.git`.
   - On the first setup of `git@gitlab.com:thorg/thorg-root.git`, run `$THORG_ROOT/init.sh`.
3. Create a `Jenkinsfile` that:
   - Properly sources the THORG Bash environment (located at `$THORG_ROOT/shell/env/thorg-env.sh`).
   - Sets up the required environment variables (`THORG_ROOT`).
   - Runs the `$THORG_ROOT/sanity_check.sh` script.
   - Implements proper error handling to catch interrupt signals from `$THORG_ROOT/sanity_check.sh` and record those as errors.
4. Provide Docker configuration files and helper scripts for:
   - Building a custom Jenkins image with necessary plugins.
   - Starting and stopping the Jenkins container.
   - Configuration as Code for initial Jenkins setup.
5. Include documentation on:
   - How to set up and run the Jenkins container using the provided code.
   - How to configure the pipeline in Jenkins.
   - How to customize the `Jenkinsfile`.
   - Troubleshooting common issues.

## Technical Details
- Bash environment must be sourced before running any scripts (requires Bash 5.x+).
- Custom Bash functions like `eai` are used in the build process; these are present in `$THORG_ROOT/shell/env/thorg-env.sh` (which can only be sourced after `$THORG_ROOT/init.sh` has been run).
- Jenkins should run in a Docker container for portability.

Please provide a complete solution, including all necessary files, scripts, and documentation, for implementing this Jenkins setup.

