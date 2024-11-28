package com.glassthought.sandbox

import java.time.Instant

data class SomeClass(val whenCreated: String)

private var singleInstance: SomeClass? = null

fun functionThatCreatesASingleInstance(): SomeClass {
  if (singleInstance == null) {
    singleInstance = SomeClass(Instant.now().toString())
  }

  return singleInstance!!
}
