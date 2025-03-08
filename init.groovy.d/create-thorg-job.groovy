import jenkins.model.Jenkins
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import hudson.triggers.SCMTrigger
import hudson.plugins.git.GitSCM
import hudson.plugins.git.UserRemoteConfig
import hudson.plugins.git.BranchSpec
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import com.cloudbees.hudson.plugins.folder.*

// Get Jenkins instance
def jenkins = Jenkins.getInstance()

// Define job name
def jobName = 'thorg-sanity-check'

// Check if job already exists
def job = jenkins.getItem(jobName)

// If job doesn't exist, create it
if (job == null) {
    println "Creating pipeline job: ${jobName}"

    // Create the job
    job = jenkins.createProject(WorkflowJob.class, jobName)

    // Define Git SCM
    def gitScm = new GitSCM(
        [new UserRemoteConfig('git@gitlab.com:thorg/thorg-root.git', null, null, 'gitlab-ssh-key')],
        [new BranchSpec('*/master-sandbox')],
        false,
        [],
        null,
        null,
        []
    )

    // Set the pipeline definition using SCM
    job.setDefinition(new CpsScmFlowDefinition(gitScm, 'Jenkinsfile'))

    // Set up SCM polling trigger to check every 1 minute
    job.addTrigger(new SCMTrigger('H/1 * * * *'))

    // Save the job
    job.save()

    println "Job '${jobName}' created successfully with SCM polling configured to check every minute."
} else {
    println "Job '${jobName}' already exists. Updating configuration..."
    
    // Update the existing job with proper SCM polling
    def gitScm = new GitSCM(
        [new UserRemoteConfig('git@gitlab.com:thorg/thorg-root.git', null, null, 'gitlab-ssh-key')],
        [new BranchSpec('*/master-sandbox')],
        false,
        [],
        null,
        null,
        []
    )
    
    // Set the pipeline definition using SCM
    job.setDefinition(new CpsScmFlowDefinition(gitScm, 'Jenkinsfile'))
    
    // Update SCM polling trigger
    def triggers = job.getTriggers()
    def oldTrigger = triggers.get(SCMTrigger.class)
    if (oldTrigger != null) {
        job.removeTrigger(SCMTrigger.class)
    }
    job.addTrigger(new SCMTrigger('H/1 * * * *'))
    
    job.save()
    
    println "Updated existing job '${jobName}' with proper SCM polling configuration to check every minute."
}
