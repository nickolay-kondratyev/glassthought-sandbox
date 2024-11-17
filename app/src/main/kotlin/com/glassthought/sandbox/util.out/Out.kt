package gt.sandbox.util.output

import gt.sandbox.util.output.impl.OutImpl

interface Out {
    suspend fun print(msg: String)
    suspend fun println(msg: String)
    suspend fun printInGreen(msg: String)
    suspend fun printInRed(msg: String)
    suspend fun printInBlue(msg: String)

    suspend fun printlnBlue(msg: String)
    suspend fun printlnGreen(msg: String)
    suspend fun printlnRed(msg: String)

    companion object {
        fun standard(): Out {
            return OutImpl()
        }
    }
}
