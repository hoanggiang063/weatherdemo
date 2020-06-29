package com.architecture.repository.core.mapper

import android.util.Log
import com.architecture.business.core.exception.BaseException
import com.architecture.business.core.exception.BusinessException
import com.architecture.business.core.exception.TechnicalException
import com.architecture.business.core.exception.UnknownException
import com.architecture.repository.core.model.ErrorModel
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

open class BaseExceptionMapperImpl : ExceptionMapper {

    override fun transform(input: Throwable?): BaseException {
        return when (input) {
            null -> UnknownException()
            is IOException -> TechnicalException()
            is HttpException -> {
                filterException(input)
            }
            else -> UnknownException()
        }
    }

    private fun filterException(exception: HttpException): BaseException {
        var result: BaseException = TechnicalException()
        if (isBusinessException(exception)) {
            val value = exception.response()?.errorBody()

            value?.let {
                val errorModel = Gson().fromJson<ErrorModel>(
                    getStringFromResponseBody(value),
                    ErrorModel::class.java
                )

                val business = BusinessException()
                business.businessCode = errorModel.errorCode
                business.businessMessage = errorModel.errorMessage

                result = business
            }

        }
        result.mCode = exception.code().toString()
        result.mMessage = exception.message()

        return result
    }

    private fun isBusinessException(exception: HttpException): Boolean {
        return HttpStatus.BAD_REQUEST.value() == exception.response()?.code() ||
                HttpStatus.NOT_FOUND.value() == exception.response()?.code()
    }

    private fun getStringFromResponseBody(responseBody: ResponseBody): String? {
        var reader: BufferedReader? = null
        val sb = StringBuilder()
        try {
            reader =
                BufferedReader(InputStreamReader(responseBody.byteStream(), "UTF8"))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                sb.append(line)
            }
        } catch (e: IOException) {
            Log.e(e.toString(), "Get string error")
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    Log.e(e.toString(), "Get string error")
                }
            }
        }
        return sb.toString()
    }
}