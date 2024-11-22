package gt.sandbox.util.output

import gt.sandbox.util.output.impl.OutImpl
import gt.sandbox.util.output.impl.OutSettings

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
    fun standard(outSettings: OutSettings = OutSettings()): Out {
      return OutImpl(outSettings)
    }
  }
}
