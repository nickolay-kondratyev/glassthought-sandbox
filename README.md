## About
Code compose-ui branch for glassthought.com and others.

### Initialization notes
[Init notes](doc/creation-notes.md)

### Helpful scripts:
- [snapshot.sh](snapshot.sh) - Snapshots the change assuming you have the already ran the application in Android Studio. `gt.snapshot --help` for more info. 
  - Note you can provide an optional custom message to use as commit heading with `./snapshot.sh "Custom message"`
- [git.push.sh](git.push.sh) - Pushes the current branch to the remote repository.

### Changing code:
Feel free to change the code in `App.kt`. And add additional files. Also feel free to clean up files that aren't needed anymore, like something that was specific to particular sandbox.

Generally not delete contents of `gt.sandbox.util` package (unless clean up is needed), but also feel free to add new files to it or modify to make it better. 
