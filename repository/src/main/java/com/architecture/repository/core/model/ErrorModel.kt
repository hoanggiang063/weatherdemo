package com.architecture.repository.core.model

import com.google.gson.annotations.SerializedName

class ErrorModel {
    var httpCode = 0

    @SerializedName("cod")
    var errorCode: String? = null

    @SerializedName("message")
    var errorMessage: String? = null
}