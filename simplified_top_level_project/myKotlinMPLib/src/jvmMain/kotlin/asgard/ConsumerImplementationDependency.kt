package asgard

import com.asgard.DependencyInterface

class ConsumerImplementationDependency : DependencyInterface {
    override fun getInteger(): Int {
        return DependencyImplementation().getInteger()
    }
}
