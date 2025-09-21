package com.tenda.feature.contact.persistence.faker

import com.tenda.persistence.core.model.Response
import com.tenda.persistence.core.repository.PersistenceRepository
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

@OptIn(InternalSerializationApi::class)
class FakePersistenceRepository : PersistenceRepository {
    val called = mutableListOf<String>()

    var executeStringResult: String = ""
    var executeTypedResult: Response<*>? = null

    override fun open(url: String, token: String, database: String) {
        called.add("open:$url:$token:$database")
    }

    override suspend fun execute(sql: String, params: List<Any?>): String {
        called.add("executeString:$sql:$params")
        return executeStringResult
    }

    override suspend fun <T : Any> execute(sql: String, type: KClass<T>): Response<T> {
        called.add("executeTyped:$sql:$type")
        @Suppress("UNCHECKED_CAST")
        return executeTypedResult as? Response<T>
            ?: error("No return value set for execute(sql, type)")
    }

    override suspend fun <T : Any> execute(
        sql: String,
        params: List<Any?>,
        type: KClass<T>
    ): Response<T> {
        called.add("executeTypedWithParams:$sql:$params:$type")
        return parseExecuteStringResult(type)
    }

    override suspend fun synchronise() {
        called.add("synchronise")
    }

    override fun close() {
        called.add("close")
    }

    private fun <T : Any> parseExecuteStringResult(type: KClass<T>): Response<T> {
        val serializer: KSerializer<ResponseModel> = ResponseModel::class.serializer()
        val parsed = Json.decodeFromString(serializer, executeStringResult)
        return Response(
            changes = parsed.changes,
            data = parsed.data.map { Json.decodeFromJsonElement(type.serializer(), it) }
        )
    }

    @Serializable
    data class ResponseModel(
        val changes: Long,
        val data: List<JsonElement>
    )
}
