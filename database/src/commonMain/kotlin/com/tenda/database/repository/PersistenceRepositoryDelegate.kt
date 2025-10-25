package com.tenda.database.repository

import com.tenda.database.exception.ExceptionHandler
import com.tenda.database.mapper.mapFromDomain
import com.tenda.database.model.ResponseModel
import com.tenda.persistence.core.model.Response
import com.tenda.persistence.core.repository.PersistenceRepository
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.koin.core.annotation.Single
import uniffi.database.Credential
import uniffi.database.PersistenceInterface
import kotlin.reflect.KClass

@Single
@OptIn(InternalSerializationApi::class)
class PersistenceRepositoryDelegate(
    private val json: Json,
    private val persistence: PersistenceInterface
) : PersistenceRepository, ExceptionHandler by ExceptionHandler.Delegate(json) {
    override suspend fun open(url: String, token: String, database: String) = launch {
        persistence.connect(
            Credential(
                url = url,
                token = token,
                database = database
            )
        )
    }

    override suspend fun execute(sql: String, params: List<Any?>): String = launch {
        persistence.execute(sql, params.mapFromDomain())
    }

    override suspend fun <T : Any> execute(sql: String, type: KClass<T>): Response<T> = launch {
        execute(sql, emptyList(), type)
    }

    override suspend fun <T : Any> execute(
        sql: String,
        params: List<Any?>,
        type: KClass<T>
    ): Response<T> = launch {
        val content = persistence.execute(sql, params.mapFromDomain())
        val response = json.decodeFromString<ResponseModel>( content)
        val typedData: List<T> = response.data.map { jsonElement ->
            json.decodeFromJsonElement(type.serializer(), jsonElement)
        }
        Response(
            changes = response.changes,
            data = typedData
        )
    }

    override suspend fun synchronise() = launch {
        persistence.synchronise()
    }

    override fun close() {
        return persistence.disconnect()
    }
}
