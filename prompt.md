# Technical Prompt: Jenkins Integration for THORG using Kotlin DSL

## Project Context: THORG
- Git repository: git@gitlab.com:thorg/thorg-root.git
  -  git@gitlab.com:thorg/thorg-root.git will be $THORG_ROOT.

## Requirements
0. Minimize manual steps.
1. Set up Jenkins in Docker to run automated sanity checks on the THORG repository
2. Setup git@gitlab.com:thorg/thorg-root.git as $THORG_ROOT
   3. Expect the SSH key on the box to have access to git@gitlab.com:thorg/thorg-root.git
   4. On the first setup of git@gitlab.com:thorg/thorg-root.git run $THORG_ROOT/init.sh
2. Create a Jenkinsfile using Kotlin DSL (instead of Groovy DSL) that:
    - Properly sources the THORG bash environment (located at $THORG_ROOT/shell/env/thorg-env.sh)
    - Sets up the required environment variables (THORG_ROOT)
    - Runs the $THORG_ROOT/sanity_check.sh script
    - Implements proper error handling for being able to catch interrupt signals from $THORG_ROOT/sanity_check.sh and record those as errors.

3. Provide Docker configuration files and helper scripts for:
    - Building a custom Jenkins image with necessary plugins
    - Starting and stopping the Jenkins container
    - Configuration as Code for initial Jenkins setup

4. Include documentation on:
    - How to set up and run the Jenkins container, using the code provided
    - How to configure the pipeline in Jenkins, using the Kotlin DSL
    - How to customize the Jenkinsfile using Kotlin DSL
    - Troubleshooting common issues

## Technical Details
- Bash environment must be sourced before running any scripts (requires bash 5.x+)
- Custom bash functions like 'eai' are used in the build process these are present from $THORG_ROOT/shell/env/thorg-env.sh (which can only be sourced after $THORG_ROOT/init.sh has been ran)
- Jenkins should run in a Docker container for portability
- Kotlin DSL should be used instead of traditional Groovy DSL for the Jenkinsfile

Please provide a complete solution including all necessary files, scripts, and documentation for implementing this Jenkins setup with Kotlin DSL.

## About Me
- Software engineer with 12 years of experience
- Experienced with Java, Kotlin, Shell, and AWS
- Working on MacOS with IntelliJ IDEA and Bash shell
