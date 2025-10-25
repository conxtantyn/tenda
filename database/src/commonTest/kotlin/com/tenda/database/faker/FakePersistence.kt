package com.tenda.database.faker

import uniffi.database.Credential
import uniffi.database.Entry
import uniffi.database.PersistenceInterface
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object FakePersistence : PersistenceInterface {
    private val mutex = Mutex()
    val calls = mutableListOf<String>()
    var executeResult: String = """{"changes":0,"data":[]}"""
    private val sqlResults: MutableMap<String, String> = mutableMapOf()

    override suspend fun connect(credential: Credential) {
        calls.add("connect:${credential.database}@${credential.token}")
    }

    override suspend fun execute(
        sql: String,
        args: List<Entry>
    ): String {
        return mutex.withLock {
            calls.add("execute:$sql:$args")
            sqlResults[sql] ?: executeResult
        }
    }

    override suspend fun synchronise() {
        calls.add("synchronise")
    }

    override fun disconnect() {
        calls.add("disconnect")
    }
}
