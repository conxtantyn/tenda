package com.tenda.common.schema

sealed class Paging(
    val index: Int,
    val size: Int
) {
    data class Request(
        private val offset: Int,
        private val limit: Int
    ) : Paging(offset, limit)

    data class Response<T>(
        private val offset: Int,
        private val limit: Int,
        val items: List<T>
    ) : Paging(offset, limit)
}
