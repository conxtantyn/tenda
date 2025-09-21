package com.tenda.feature.contact.persistence.delegate

import com.tenda.common.schema.Paging
import com.tenda.feature.contact.data.persistence.ContactPersistence
import com.tenda.feature.contact.domain.model.ContactDetail
import com.tenda.feature.contact.persistence.faker.FakePersistenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ContactPersistenceDelegateTest {
    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private val repository = FakePersistenceRepository()

    private lateinit var persistence: ContactPersistence

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        persistence = ContactPersistenceDelegate(repository)
    }

    @AfterTest
    fun tearDown() {
        repository.close()
        Dispatchers.resetMain()
    }

    @Test
    fun `initialize executes create table`() = testScope.runTest {
        persistence.initialize()
        assertTrue(repository.called.any {
            it.startsWith("executeString:CREATE TABLE IF NOT EXISTS contacts")
        })
    }

    @Test
    fun `get returns single contact`() = testScope.runTest {
        val contactJson = buildJsonArray {
            add(buildJsonObject {
                put("id", "1")
                put("name", "Alice")
                put("logo", "logo.png")
            })
        }
        repository.executeStringResult = """{"changes":1,"data":$contactJson}"""

        val result = persistence.get("1")

        assertEquals("1", result.id)
        assertEquals("Alice", result.name)
        assertEquals("logo.png", result.logo)
    }

    @Test
    fun `getAll returns paging response`() = testScope.runTest {
        val contactsJson = buildJsonArray {
            add(buildJsonObject {
                put("id", "1")
                put("name", "Alice")
                put("logo", "logo1.png")
            })
            add(buildJsonObject {
                put("id", "2")
                put("name", "Bob")
                put("logo", "logo2.png")
            })
        }
        repository.executeStringResult = """{"changes":2,"data":$contactsJson}"""

        val paging = Paging.Request(offset = 0, limit = 10)
        val response = persistence.getAll(paging)

        assertEquals(0, response.index)
        assertEquals(2, response.size)
        assertEquals("Alice", response.items[0].name)
        assertEquals("Bob", response.items[1].name)
    }

    @Test
    fun `create executes insert and returns contact`() = testScope.runTest {
        val contactJson = buildJsonArray {
            add(buildJsonObject {
                put("id", "1")
                put("name", "Charlie")
                put("logo", "")
            })
        }
        repository.executeStringResult = """{"changes":1,"data":$contactJson}"""

        val result = persistence.create("Charlie")

        // Insert call + select call
        assertTrue(repository.called.any {
            it.startsWith("executeString:INSERT INTO contacts")
        })
        assertEquals("Charlie", result.name)
        assertEquals("1", result.id)
    }

    @Test
    fun `update executes update and returns contact`() = testScope.runTest {
        val contactJson = buildJsonArray {
            add(buildJsonObject {
                put("id", "1")
                put("name", "Delta")
                put("logo", "logo.png")
            })
        }
        repository.executeStringResult = """{"changes":1,"data":$contactJson}"""

        val detail = ContactDetail(name = "Delta", logo = "logo.png")
        val result = persistence.update(detail)

        assertTrue(repository.called.any {
            it.startsWith("executeString:UPDATE contacts")
        })
        assertEquals("Delta", result.name)
        assertEquals("1", result.id)
    }

    @Test
    fun `delete executes delete statement`() = testScope.runTest {
        persistence.delete("1")
        assertTrue(repository.called.any {
            it.startsWith("executeString:DELETE FROM contacts")
        })
    }

    @Test
    fun `synchronise calls repository synchronise`() = testScope.runTest {
        persistence.synchronise()
        assertTrue(repository.called.contains("synchronise"))
    }
}
