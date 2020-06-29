package com.architecture.business.core.exception

open class BaseException : Throwable() {
    var mCode: String? = null
    var mMessage: String? = null
}