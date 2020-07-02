package com.architecture.cleanmvvm.core.security

import com.jaredrummler.android.shell.CommandResult
import com.jaredrummler.android.shell.Shell
import java.io.File
import java.lang.Double.parseDouble


class SecurityMonitor() {

    fun isSafeEnvironment(): Boolean {
        return checkRootMethodOne() || checkRootMethodTwo() || isRootbyMagisk()
    }

    private fun checkRootMethodOne(): Boolean {
        val buildTags = android.os.Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethodTwo(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        for (path in paths) {
            if (File(path).exists()) return true
        }
        return false
    }

    private fun isRootbyMagisk(): Boolean {

        val result: CommandResult = Shell.SH.run("magisk -V")
        return result.isSuccessful && isNumeric(result.toString())
    }

    private fun isNumeric(input: String): Boolean {
        var numeric = true
        try {
            parseDouble(input)
        } catch (e: NumberFormatException) {
            numeric = false
        }

        return numeric
    }
}