package com.tenda.feature.contact.persistence.delegate

import com.tenda.common.schema.Paging
import com.tenda.feature.contact.data.persistence.ContactPersistence
import com.tenda.feature.contact.domain.model.Contact
import com.tenda.feature.contact.domain.model.ContactDetail
import com.tenda.feature.contact.persistence.mapper.mapToDomain
import com.tenda.feature.contact.persistence.model.ContactModel
import com.tenda.persistence.core.repository.PersistenceRepository
import org.koin.core.annotation.Single

@Single
class ContactPersistenceDelegate(
    private val repository: PersistenceRepository
) : ContactPersistence {
    override suspend fun initialize() {
        repository.execute("""
            CREATE TABLE IF NOT EXISTS contacts (
                id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))),
                name TEXT NOT NULL,
                logo TEXT NOT NULL,
                created_at TEXT NOT NULL DEFAULT (CURRENT_TIMESTAMP)
            );
        """.trimIndent())
    }

    override suspend fun get(id: String): Contact {
        return repository.execute(
            sql = """
                SELECT * FROM contacts WHERE id = ?
            """.trimIndent(),
            params = listOf(id),
            type = ContactModel::class
        ).data.first().mapToDomain()
    }

    override suspend fun getAll(paging: Paging.Request): Paging.Response<Contact> {
        val response = repository.execute(
            sql = """
                SELECT * FROM contacts 
                ORDER BY created_at DESC 
                LIMIT ? OFFSET ?;
            """.trimIndent(),
            params = listOf(paging.size, paging.index),
            type = ContactModel::class
        )
        return Paging.Response(
            offset = paging.index,
            limit = response.data.size,
            items = response.data.map { it.mapToDomain() }
        )
    }

    override suspend fun getAllByName(
        name: String,
        paging: Paging.Request
    ): Paging.Response<Contact> {
        val response = repository.execute(
            sql = """
                SELECT * FROM contacts WHERE name LIKE ? LIMIT ? OFFSET ?
            """.trimIndent(),
            params = listOf(name, paging.size, paging.index),
            type = ContactModel::class
        )
        return Paging.Response(
            offset = paging.index,
            limit = response.data.size,
            items = response.data.map { it.mapToDomain() }
        )
    }

    override suspend fun create(name: String): Contact {
        repository.execute(
            sql = """
                INSERT INTO contacts (name, logo) VALUES (?, ?)
            """.trimIndent(),
            params = listOf(name, "")
        )
        return repository.execute(
            sql = """
                SELECT * FROM contacts WHERE name = ?
            """.trimIndent(),
            params = listOf(name),
            type = ContactModel::class
        ).data.first().mapToDomain()
    }

    override suspend fun update(detail: ContactDetail): Contact {
        repository.execute(
            sql = """
                UPDATE contacts SET name = ?, logo = ? WHERE id = ?
            """.trimIndent(),
            params = listOf(detail.name, detail.logo)
        )
        return repository.execute(
            sql = """
                SELECT * FROM contacts WHERE name = ?
            """.trimIndent(),
            params = listOf(detail.name),
            type = ContactModel::class
        ).data.first().mapToDomain()
    }

    override suspend fun synchronise() {
        repository.synchronise()
    }

    override suspend fun delete(id: String) {
        repository.execute(
            sql = """
                DELETE FROM contacts WHERE id = ?
            """.trimIndent(),
            params = listOf(id)
        )
    }
}
