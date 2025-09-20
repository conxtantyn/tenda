package com.tenda.database.repository

import com.tenda.database.mapper.mapFromDomain
import com.tenda.database.model.ResponseModel
import com.tenda.persistence.core.model.Response
import com.tenda.persistence.core.repository.PersistenceRepository
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import uniffi.database.Persistence
import kotlin.reflect.KClass

@OptIn(InternalSerializationApi::class)
class PersistenceRepositoryDelegate(
    private val json: Json,
    private val persistence: Persistence
) : PersistenceRepository {
    override suspend fun open(database: String) {
        return persistence.connect(database)
    }

    override suspend fun execute(sql: String, params: List<Any?>): String {
        return persistence.execute(sql, params.mapFromDomain())
    }

    override suspend fun <T : Any> execute(
        sql: String,
        kClass: KClass<T>,
        params: List<Any?>
    ): Response<T> {
        val content = persistence.execute(sql, params.mapFromDomain())
        val response = json.decodeFromString<ResponseModel>( content)
        val typedData: List<T> = response.data.map { jsonElement ->
            json.decodeFromJsonElement(kClass.serializer(), jsonElement)
        }
        return Response(
            changes = response.changes,
            data = typedData
        )
    }

    override suspend fun close() {
        return persistence.disconnect()
    }
}
