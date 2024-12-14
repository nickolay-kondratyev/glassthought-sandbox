package gt.sandbox.util.output

import com.glassthought.sandbox.sandbox.util.out.impl.OutImpl
import com.glassthought.sandbox.sandbox.util.out.impl.OutSettings


interface Out {
  fun print(msg: String)
  fun println(msg: String)
  fun printGreen(msg: String)
  fun printRed(msg: String)
  fun printBlue(msg: String)

  fun infoNonSuspend(msg: String)
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

val out = Out.standard(OutSettings(printCoroutineName = true))

fun <T> T.printGreen(): T {
  out.printGreen(this.toString())
  return this
}

fun <T> T.print(): T {
  out.print(this.toString())
  return this
}

fun <T> T.println(): T {
  out.println(this.toString())
  return this
}
