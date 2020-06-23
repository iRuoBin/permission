package com.iruobin.android.permission

import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log

/**
 * 打印日志
 * @author iRuoBin
 * @since 0.1
 */
object PrintLog {

    /**
     * 是否打印 Log 的控制开关
     */
    var isPrint = false

    /**
     * tag 加上 前缀来区分 是系统 log 还是 自己 打印的 log
     */
    var prefix = "iRuoBin -> "

    /**
     * 打印长 log 时，每段打印的字符数（系统 Log 类上限是 4 * 1024 个字符，实际输出比这个数值略小）
     */
    var showLength = 3600

    @JvmStatic
    fun init(context: Context?) {
        isPrint = debugAble(context)
    }

    @JvmStatic
    fun debugAble(context: Context?): Boolean {
        return (context?.applicationInfo?.flags ?: 0 and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    private fun getTag(description: String = ""): String {
        val trace = Throwable().fillInStackTrace().stackTrace
        var fileName = ""
        var lineNumber = 0
        for (i in 2 until trace.size) {
            val className = trace[i].className
            if (className != this.javaClass.name) {
                fileName = trace[i].fileName
                lineNumber = trace[i].lineNumber
                break
            }
        }
        // (fileName:lineNumber) 只有这种格式在 logcat 中才可以点击跳转
        return "$prefix$description($fileName:$lineNumber)"
    }

    private fun buildMessage(msg: String): String {
        val trace = Throwable().fillInStackTrace().stackTrace
        var methodName = ""
        for (i in 2 until trace.size) {
            val className = trace[i].className
            if (className != this.javaClass.name) {
                methodName = trace[i].methodName
                break
            }
        }
        return "[${Thread.currentThread().id}] $methodName: $msg"
    }

    @JvmStatic
    fun v(msg: String) {
        if (isPrint) {
            showLargeLog(msg) { tag, msg -> Log.v(tag, msg) }
        }
    }

    @JvmStatic
    fun d(msg: String) {
        if (isPrint) {
            showLargeLog(msg) { tag, msg -> Log.d(tag, msg) }
        }
    }

    @JvmStatic
    fun i(msg: String) {
        if (isPrint) {
            showLargeLog(msg) { tag, msg -> Log.i(tag, msg) }
        }
    }

    @JvmStatic
    fun w(msg: String) {
        if (isPrint) {
            showLargeLog(msg) { tag, msg -> Log.w(tag, msg) }
        }
    }

    @JvmStatic
    fun e(msg: String) {
        if (isPrint) {
            showLargeLog(msg) { tag, msg -> Log.e(tag, msg) }
        }
    }

    /**
     * 分段打印较长的 log 文本
     */
    private fun showLargeLog(msg: String, printLog: (String, String) -> Unit) {
        val loopCount = if (msg.length % showLength == 0) msg.length / showLength else msg.length / showLength + 1
        for (i in 1..loopCount) {
            val partLog = msg.substring(showLength * (i - 1), if (showLength * i > msg.length) msg.length else showLength * i)
            printLog(if (loopCount > 1) getTag("分段打印 共${loopCount}段，目前是 第${i}段") else getTag(), buildMessage(partLog))
        }
    }
}