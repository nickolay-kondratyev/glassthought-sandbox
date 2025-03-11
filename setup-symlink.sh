_remake_symlink(){
  local src="${1:?}"
  local dest="${2:?}"
  local tmp_dest="${dest:?}.tmp"

  if [[ ! -e "${src:?}" ]]; then
    echo "Error: Source does not exist. src=[${src:?}]"
    return 1
  fi

  if [[ -e "${dest:?}" ]]; then
    rm "${dest:?}"
  fi

  ln -s "${src:?}" "${tmp_dest:?}"
  mv "${tmp_dest:?}" "${dest:?}"
}

# Take env variable like JAVA_17_HOME, make sure it points to a java home dir
# and make a symlink for it under home directory ${HOME:?}/symlinks/JAVA_17_HOME
_make_java_symlink_based_off_env_variable(){
  mkdir -p "${HOME:?}"/symlinks

  local env_var_name="${1:?}"
  local java_home_dir="${!env_var_name:?}"

  if [[ ! -d "${java_home_dir:?}" ]]; then
    echo "Error: ${env_var_name:?} is not a directory. ${env_var_name:?}=[${java_home_dir:?}]"
    return 1
  fi

  _remake_symlink "${java_home_dir:?}" "${HOME:?}/symlinks/${env_var_name:?}"
}

_make_symlink_for_jenkins_home_to_see_it_in_out_dir(){
    mkdir -p ./out
    _remake_symlink "${HOME:?}"/.gradle ./out/.gradle
}

main() {
  _make_symlink_for_jenkins_home_to_see_it_in_out_dir

  _make_java_symlink_based_off_env_variable JAVA_17_HOME
  _make_java_symlink_based_off_env_variable JAVA_8_HOME
}

main "${@}" || exit 1
