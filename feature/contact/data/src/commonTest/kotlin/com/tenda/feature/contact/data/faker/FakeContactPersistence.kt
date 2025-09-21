package com.tenda.feature.contact.data.faker

import com.tenda.common.schema.Paging
import com.tenda.feature.contact.data.persistence.ContactPersistence
import com.tenda.feature.contact.domain.model.Contact
import com.tenda.feature.contact.domain.model.ContactDetail

class FakeContactPersistence : ContactPersistence {
    var calledMethods = mutableListOf<String>()
    var getReturn: Contact? = null
    var getAllReturn: Paging.Response<Contact>? = null
    var getAllByNameReturn: Paging.Response<Contact>? = null
    var createReturn: Contact? = null
    var updateReturn: Contact? = null

    override suspend fun initialize() {
        calledMethods.add("initialize")
    }

    override suspend fun get(id: String): Contact {
        calledMethods.add("get:$id")
        return getReturn ?: error("No return value set for get")
    }

    override suspend fun getAll(paging: Paging.Request): Paging.Response<Contact> {
        calledMethods.add("getAll:$paging")
        return getAllReturn ?: error("No return value set for getAll")
    }

    override suspend fun getAllByName(
        name: String,
        paging: Paging.Request
    ): Paging.Response<Contact> {
        calledMethods.add("getAllByName:$name:$paging")
        return getAllByNameReturn ?: error("No return value set for getAllByName")
    }

    override suspend fun create(name: String): Contact {
        calledMethods.add("create:$name")
        return createReturn ?: error("No return value set for create")
    }

    override suspend fun update(detail: ContactDetail): Contact {
        calledMethods.add("update:$detail")
        return updateReturn ?: error("No return value set for update")
    }

    override suspend fun synchronise() {
        calledMethods.add("synchronise")
    }

    override suspend fun delete(id: String) {
        calledMethods.add("delete:$id")
    }
}
