package com.tenda.persistence.core.repository

import com.tenda.persistence.core.model.Response
import kotlin.reflect.KClass

interface PersistenceRepository {
    suspend fun open(database: String)

    suspend fun execute(sql: String, params: List<Any?> = emptyList()): String

    suspend fun<T : Any> execute(
        sql: String,
        kClass: KClass<T>,
        params: List<Any?> = emptyList()
    ): Response<T>

    suspend fun close()
}
