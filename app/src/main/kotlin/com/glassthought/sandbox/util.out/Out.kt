package gt.sandbox.util.output

import gt.sandbox.util.output.impl.OutImpl
import gt.sandbox.util.output.impl.OutSettings

interface Out {
  fun print(msg: String)
  fun printGreen(msg: String)
  fun printRed(msg: String)
  fun printBlue(msg: String)

  suspend fun println(msg: String)
  suspend fun printlnBlue(msg: String)
  suspend fun printlnGreen(msg: String)
  suspend fun printlnRed(msg: String)

  companion object {
    fun standard(outSettings: OutSettings = OutSettings()): Out {
      return OutImpl(outSettings)
    }
  }
}
