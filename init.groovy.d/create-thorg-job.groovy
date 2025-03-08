import com.cloudbees.hudson.plugins.folder.*
import hudson.plugins.git.BranchSpec
import hudson.plugins.git.GitSCM
import hudson.plugins.git.UserRemoteConfig
import hudson.triggers.SCMTrigger
import jenkins.model.Jenkins
import org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob

// Function to create Git SCM configuration
def createGitScm() {
    return new GitSCM(
        [new UserRemoteConfig('git@gitlab.com:thorg/thorg-root.git', null, null, 'gitlab-ssh-key')],
        [new BranchSpec('*/master-sandbox')],
        false,
        [],
        null,
        null,
        []
    )
}

// Function to configure SCM polling trigger
def configureSCMTrigger(job) {
    // Clear any existing triggers first
    job.getTriggers().clear()
    
    // Configure SCM polling with optimized settings
    SCMTrigger trigger = new SCMTrigger('* * * * *')
    trigger.setIgnorePostCommitHooks(false)
    
    // Add the new trigger
    job.addTrigger(trigger)
}

// Function to configure job with pipeline definition and SCM trigger
def configureJob(job) {
    // Define Git SCM
    def gitScm = createGitScm()
    
    // Set the pipeline definition using SCM
    job.setDefinition(new CpsScmFlowDefinition(gitScm, 'Jenkinsfile'))
    
    // Disable concurrent builds
    job.setConcurrentBuild(false)
    
    // Configure SCM polling
    configureSCMTrigger(job)
    
    // Save the job
    job.save()
}

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
    
    // Configure the job
    configureJob(job)
    
    println "Job '${jobName}' created successfully with optimized SCM polling configured."
} else {
    println "Job '${jobName}' already exists. Updating configuration..."
    
    // Update the existing job
    configureJob(job)
    
    println "Updated existing job '${jobName}' with optimized SCM polling configuration."
}
