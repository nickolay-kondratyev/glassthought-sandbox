package asgard

import com.asgard.InterfaceFromDependency

class JVMImplementationFromDependency : InterfaceFromDependency {
    override fun getInteger(): Int {
        return 42
    }
}
