source "./_env_setup_source_me.sh"

main() {
  GIT_SSH_COMMAND="ssh -i ${JENKINS_HOME:?}/.ssh/id_rsa" git clone "git@gitlab.com:thorg/thorg-root.git" /tmp/thorg-root-"$(gnu_date +%s%3N)"
}

main "${@}" || exit 1
