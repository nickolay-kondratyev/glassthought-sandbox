package com.glassthought.sandbox.util.out

import com.glassthought.sandbox.util.out.impl.OutImpl
import com.glassthought.sandbox.util.out.impl.OutSettings

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
