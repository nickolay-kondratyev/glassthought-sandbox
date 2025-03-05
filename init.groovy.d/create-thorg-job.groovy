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
    
    // Define the pipeline script
    def pipelineScript = '''
pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps {
        // Checkout the repository with specific credentials
        checkout([
          $class: 'GitSCM',
          branches: [[name: '*/master']],
          userRemoteConfigs: [[
            url: 'git@gitlab.com:thorg/thorg-root.git',
            credentialsId: 'gitlab-ssh-key'
          ]]
        ])
      }
    }

    stage('Execute Shell Script') {
      steps {
        // Execute the script with Bash explicitly
        sh 'bash ./say_hello.sh'
      }
    }
  }

  post {
    success {
      echo 'Pipeline executed successfully!'
    }
    failure {
      echo 'Pipeline execution failed!'
    }
  }
}
'''
    
    // Set the pipeline definition
    job.setDefinition(new CpsFlowDefinition(pipelineScript, true))
    
    // Add SCM trigger to run every 15 minutes
    job.addTrigger(new SCMTrigger('H/15 * * * *'))
    
    // Save the job
    job.save()
    
    println "Job '${jobName}' created successfully."
} else {
    println "Job '${jobName}' already exists. Skipping creation."
} 