package gt.kotlin.sandbox.extensionMethods

import gt.kotlin.sandbox.Student

fun Student.getAge(): String {
    return this.getName() + " is 20 years old"
}
