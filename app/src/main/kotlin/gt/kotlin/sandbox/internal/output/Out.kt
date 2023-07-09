package gt.kotlin.sandbox.internal.output

import gt.kotlin.sandbox.internal.output.impl.OutImpl

interface Out {
    fun print(msg: String)
    fun println(msg: String)
    fun printInGreen(msg: String)
    fun printInRed(msg: String)
    fun printInBlue(msg: String)

    fun printlnBlue(msg: String)
    fun printlnGreen(msg: String)
    fun printlnRed(msg: String)

    companion object {
        fun standard(): Out {
            return OutImpl()
        }
    }
}
