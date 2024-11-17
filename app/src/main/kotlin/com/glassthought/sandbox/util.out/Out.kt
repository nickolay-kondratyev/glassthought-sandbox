package gt.sandbox.util.output

import gt.sandbox.util.output.impl.OutImpl
import gt.sandbox.util.output.impl.OutSettings

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
        fun standard(outSettings: OutSettings = OutSettings()): Out {
            return OutImpl(outSettings)
        }
    }
}
