package com.architecture.business.core.callback

import com.architecture.business.core.exception.TechnicalException

interface BasePresentCallBack<Result> {
    fun onSuccess(expectedResult: Result?)
    fun onFailByTechnical(exception: TechnicalException)
    fun onDefaultFail(throwable: Throwable)
}