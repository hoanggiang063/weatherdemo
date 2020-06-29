package com.architecture.business.core.repository

interface BaseRepository<Param, Result> {
    fun setParam(param: Param)
    suspend operator fun invoke(): Result
}