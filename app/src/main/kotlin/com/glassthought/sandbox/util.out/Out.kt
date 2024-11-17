package gt.sandbox.util.output

import gt.sandbox.util.output.impl.OutImpl
import gt.sandbox.util.output.impl.OutSettings

interface Out {
  fun print(msg: String)
  fun printGreen(msg: String)
  fun printRed(msg: String)
  fun printBlue(msg: String)

  suspend fun info(msg: String)
  suspend fun infoBlue(msg: String)
  suspend fun infoGreen(msg: String)
  suspend fun infoRed(msg: String)

  companion object {
    fun standard(outSettings: OutSettings = OutSettings()): Out {
      return OutImpl(outSettings)
    }
  }
}
