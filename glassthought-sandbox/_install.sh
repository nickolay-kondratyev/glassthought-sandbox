main() {
    local vsix
    # shellcheck disable=SC2012
    vsix="$(ls -tr *.vsix | tail -n 1)"

    [[ -f "${vsix}" ]] || throw "File not found: ${vsix}"

    # [Install Vsix Extension File](http://www.glassthought.com/notes/rccnh6vnp712zzr3886snot)
    eai code --install-extension "${vsix}"
    echo.green "Installed ${vsix}. "

    echo ""
    echo "Opening VSCode in 2 seconds..."
    echo ""
    echo.bold "📣 Reload VSCode to see the changes. 📣"

    sleep 2
    code --reuse-window
}

main "${@}" || exit 1
