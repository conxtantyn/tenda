package com.tenda.persistence.core.model

data class Response<T : Any>(
    val changes: Long,
    val data: List<T>
)
