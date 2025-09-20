package com.tenda.database.mock

import uniffi.database.Credential
import uniffi.database.Entry
import uniffi.database.PersistenceInterface

object FakePersistence : PersistenceInterface{
    var credential: Credential? = null
        private set

    var isSynchronized = false
        private set

    var isDisconnected = false
        private set

    override fun connect(credential: Credential) {
        this.credential = credential
    }

    override fun disconnect() {
        isDisconnected = true
    }

    override suspend fun execute(
        sql: String,
        args: List<Entry>
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun synchronise() {
        isSynchronized = true
    }
}
