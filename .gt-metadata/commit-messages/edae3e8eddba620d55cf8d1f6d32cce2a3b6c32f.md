To reproduce: 'gradle -q task-depends-on-hello' (from commit:edae3e8) in ${GT_SANDBOX_REPO}/build_system/gradle/task_dependency_eg


## Command to reproduce:
```bash
gt.sandbox.checkout.commit edae3e8 \
&& cd "${GT_SANDBOX_REPO}/build_system/gradle/task_dependency_eg" \
&& cmd.run.announce "gradle -q task-depends-on-hello"
```

## Recorded output of command:
```
Hello world!
I'm Gradle
```

