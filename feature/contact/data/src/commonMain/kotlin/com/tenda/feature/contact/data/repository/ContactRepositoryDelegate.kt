package com.tenda.feature.contact.data.repository

import com.tenda.common.schema.Paging
import com.tenda.feature.contact.data.persistence.ContactPersistence
import com.tenda.feature.contact.domain.model.Contact
import com.tenda.feature.contact.domain.model.ContactDetail
import com.tenda.feature.contact.domain.repository.ContactRepository
import org.koin.core.annotation.Single

@Single
class ContactRepositoryDelegate(
    private val persistence: ContactPersistence
) : ContactRepository {
    override suspend fun get(id: String): Contact {
        return persistence.get(id)
    }

    override suspend fun getAll(paging: Paging.Request): Paging.Response<Contact> {
        return persistence.getAll(paging)
    }

    override suspend fun getAllByName(
        name: String,
        paging: Paging.Request
    ): Paging.Response<Contact> {
        return persistence.getAllByName(name, paging)
    }

    override suspend fun create(name: String): Contact {
        return persistence.create(name)
    }

    override suspend fun update(detail: ContactDetail): Contact {
        return persistence.update(detail)
    }

    override suspend fun synchronise() {
        return persistence.synchronise()
    }

    override suspend fun delete(id: String) {
        return persistence.delete(id)
    }
}
