source "../_env_setup_source_me.sh"

cat "${JENKINS_HOME:?}/secrets/initialAdminPassword"
