package com.architecture.repository.core.mapper

import com.architecture.business.core.exception.BaseException

interface ExceptionMapper {
    fun transform(input: Throwable?): BaseException
}