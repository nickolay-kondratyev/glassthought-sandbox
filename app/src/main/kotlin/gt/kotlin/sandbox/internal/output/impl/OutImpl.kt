package gt.kotlin.sandbox.internal.output.impl

import gt.kotlin.sandbox.internal.output.Out

class OutImpl : Out {
    override fun print(msg: String) {
        kotlin.io.print(msg)
    }

    override fun println(msg: String) {
        kotlin.io.println(msg)
    }

    override fun printWithThreadInfo(msg: String) {
        kotlin.io.print("${Thread.currentThread().name}: $msg")
    }

    override fun printlnWithThreadInfo(msg: String) {
        kotlin.io.println("${Thread.currentThread().name}: $msg")
    }

    override fun printInGreen(msg: String) {
        kotlin.io.print("\u001B[32m$msg\u001B[0m")
    }

    override fun printInRed(msg: String) {
        kotlin.io.print("\u001B[31m$msg\u001B[0m")
    }

    override fun printInBlue(msg: String) {
        kotlin.io.print("\u001B[34m$msg\u001B[0m")
    }

    override fun printlnBlue(msg: String) {
        kotlin.io.println("\u001B[34m$msg\u001B[0m")
    }

    override fun printlnGreen(msg: String) {
        kotlin.io.println("\u001B[32m$msg\u001B[0m")
    }

    override fun printlnRed(msg: String) {
        kotlin.io.println("\u001B[31m$msg\u001B[0m")
    }
}
