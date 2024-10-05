### Command to reproduce:
```bash
gt.sandbox.checkout.commit b0d1f9a \
&& cd "${GT_SANDBOX_REPO}" \
&& cmd.run.announce "./gradlew lib:processData --console=plain && echo && echo output.txt: && cat ${GLASSTHOUGHT_SANDBOX:?}/lib/build/output.txt"
```

### Recorded output of command:
```txt
> Task :lib:processData UP-TO-DATE

BUILD SUCCESSFUL in 409ms
1 actionable task: 1 up-to-date

output.txt:
2024-10-05T22:17:27.708102Z VAL-2
```

