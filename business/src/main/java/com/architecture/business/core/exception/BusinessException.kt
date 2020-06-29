package com.architecture.business.core.exception

class BusinessException : BaseException() {
    companion object {
        const val DEFAULT_DB_ERROR_CODE = "01"
        const val DEFAULT_DB_ERROR_MESSAGE = "Item not found"
    }

    var businessCode: String? = null
    var businessMessage: String? = null
}