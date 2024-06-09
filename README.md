## About
Code Kotlin branch for glassthought.com and others.

### Helpful scripts:
- [snapshot.sh](snapshot.sh) - Runs `App.kt`, if the command is successful, it will commit the changes. As well as record the output and a way to reproduce the command with the change. `gt.snapshot --help` for more info. 
- [snapshot_with_AppKtCode.sh](snapshot_with_AppKtCode.sh) - like `snapshot.sh` but also copies the code of `App.kt` to the clipboard.
- [intellij.open-main-app-file.sh](intellij.open-main-app-file.sh) - Opens the main `App.kt` file in IntelliJ IDEA.
- [git.push.sh](git.push.sh) - Pushes the current branch to the remote repository.

### Changing code:
Feel free to change the code in `App.kt`. And add additional files. Also feel free to clean up files that aren't needed anymore, like something that was specific to particular sandbox.

Generally not delete contents of `gt.sandbox.util` package (unless clean up is needed), but also feel free to add new files to it or modify to make it better. 
