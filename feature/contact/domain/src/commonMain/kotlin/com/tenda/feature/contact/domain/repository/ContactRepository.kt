package com.tenda.feature.contact.domain.repository

import com.tenda.common.schema.Paging
import com.tenda.feature.contact.domain.model.Contact
import com.tenda.feature.contact.domain.model.ContactDetail

interface ContactRepository {
    suspend fun initialize()

    suspend fun get(id: String): Contact

    suspend fun getAll(paging: Paging.Request): Paging.Response<Contact>

    suspend fun getAllByName(name: String, paging: Paging.Request): Paging.Response<Contact>

    suspend fun create(name: String): Contact

    suspend fun update(detail: ContactDetail): Contact

    suspend fun synchronise()

    suspend fun delete(id: String)
}
