package gt.sandbox

import java.text.SimpleDateFormat
import java.util.*

class ThreadUtils {
    companion object {
        private var lastTime: Long = System.currentTimeMillis()

        fun printWithThreadInfo(msg: String) {
            val thread = Thread.currentThread()
            val threadName = thread.name
            val threadId = thread.id
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())
            val currentTime = System.currentTimeMillis()
            val elapsedMillis = currentTime - lastTime
            println("[$timestamp][$threadName-$threadId][${elapsedMillis}ms] $msg")
            lastTime = currentTime
        }

        fun sleep(millis: Long){
            Thread.sleep(millis)
        }
    }
}
