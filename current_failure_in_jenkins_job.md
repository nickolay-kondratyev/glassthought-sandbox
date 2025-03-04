Started by user admin
[Pipeline] Start of Pipeline
[Pipeline] node
Running on Jenkins in /Users/nickolaykondratyev/git_repos/glassthought-sandbox/out/.jenkins/workspace/thorg-sanity-check
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Checkout)
[Pipeline] checkout
The recommended git tool is: NONE
using credential gitlab-ssh-key
Cloning the remote Git repository
Cloning repository git@gitlab.com:thorg/thorg-root.git
> git init /Users/nickolaykondratyev/git_repos/glassthought-sandbox/out/.jenkins/workspace/thorg-sanity-check # timeout=10
Fetching upstream changes from git@gitlab.com:thorg/thorg-root.git
> git --version # timeout=10
> git --version # 'git version 2.39.3 (Apple Git-146)'
using GIT_SSH to set credentials SSH key for GitLab access
Verifying host key using known hosts file
> git fetch --tags --force --progress -- git@gitlab.com:thorg/thorg-root.git +refs/heads/*:refs/remotes/origin/* # timeout=10
ERROR: Error cloning remote repo 'origin'
hudson.plugins.git.GitException: Command "git fetch --tags --force --progress -- git@gitlab.com:thorg/thorg-root.git +refs/heads/*:refs/remotes/origin/*" returned status code 128:
stdout:
stderr: Load key "/Users/nickolaykondratyev/git_repos/glassthought-sandbox/out/.jenkins/workspace/thorg-sanity-check@tmp/jenkins-gitclient-ssh17060130627059384668.key": invalid format
git@gitlab.com: Permission denied (publickey).
fatal: Could not read from remote repository.

Please make sure you have the correct access rights
and the repository exists.

	at PluginClassLoader for git-client//org.jenkinsci.plugins.gitclient.CliGitAPIImpl.launchCommandIn(CliGitAPIImpl.java:2852)
	at PluginClassLoader for git-client//org.jenkinsci.plugins.gitclient.CliGitAPIImpl.launchCommandWithCredentials(CliGitAPIImpl.java:2188)
	at PluginClassLoader for git-client//org.jenkinsci.plugins.gitclient.CliGitAPIImpl$1.execute(CliGitAPIImpl.java:638)
	at PluginClassLoader for git-client//org.jenkinsci.plugins.gitclient.CliGitAPIImpl$2.execute(CliGitAPIImpl.java:880)
	at PluginClassLoader for git//hudson.plugins.git.GitSCM.retrieveChanges(GitSCM.java:1221)
	at PluginClassLoader for git//hudson.plugins.git.GitSCM._checkout(GitSCM.java:1311)
	at PluginClassLoader for git//hudson.plugins.git.GitSCM.checkout(GitSCM.java:1278)
	at PluginClassLoader for workflow-scm-step//org.jenkinsci.plugins.workflow.steps.scm.SCMStep.checkout(SCMStep.java:136)
	at PluginClassLoader for workflow-scm-step//org.jenkinsci.plugins.workflow.steps.scm.SCMStep$StepExecutionImpl.run(SCMStep.java:101)
	at PluginClassLoader for workflow-scm-step//org.jenkinsci.plugins.workflow.steps.scm.SCMStep$StepExecutionImpl.run(SCMStep.java:88)
	at PluginClassLoader for workflow-step-api//org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution.lambda$start$0(SynchronousNonBlockingStepExecution.java:49)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	at java.base/java.lang.Thread.run(Thread.java:1583)
ERROR: Error cloning remote repo 'origin'
ERROR: Maximum checkout retry attempts reached, aborting
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Execute Shell Script)
Stage "Execute Shell Script" skipped due to earlier failure(s)
[Pipeline] getContext
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Declarative: Post Actions)
[Pipeline] echo
Pipeline execution failed!
[Pipeline] }
[Pipeline] // stage
[Pipeline] }
[Pipeline] // node
[Pipeline] End of Pipeline
ERROR: Error cloning remote repo 'origin'
Finished: FAILURE
