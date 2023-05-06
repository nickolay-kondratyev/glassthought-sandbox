package asgard

import com.asgard.DependencyInterface

class DependencyImplementation : DependencyInterface {
    override fun getInteger(): Int {
        return 42000
    }
}
