package com.tenda.database.exception

import com.tenda.common.exception.DomainException
import com.tenda.database.model.ExceptionModel
import kotlinx.serialization.json.Json
import uniffi.database.InternalException

interface ExceptionHandler {
    fun<T> run(block: () -> T): T

    suspend fun<T> launch(block: suspend () -> T): T

    class Delegate(private val json: Json) : ExceptionHandler {
        override fun <T> run(block: () -> T): T {
            return try {
                block()
            } catch (error: Throwable) {
                throw parseException(error)
            }
        }

        private fun parseException(error: Throwable): Throwable {
            return (error as? InternalException?)?.message?.let {
                val exception = json.decodeFromString<ExceptionModel>(it)
                DomainException(
                    status = exception.status,
                    reason = exception.reason
                )
            } ?: error
        }

        override suspend fun <T> launch(block: suspend () -> T): T {
            return try {
                block()
            } catch (error: Throwable) {
                throw parseException(error)
            }
        }
    }
}
