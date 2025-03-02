import jenkins.model.*
import hudson.security.*
import jenkins.install.*

def instance = Jenkins.getInstance()

// Skip the setup wizard
instance.setInstallState(InstallState.INITIAL_SETUP_COMPLETED)

// Save the configuration
instance.save() 