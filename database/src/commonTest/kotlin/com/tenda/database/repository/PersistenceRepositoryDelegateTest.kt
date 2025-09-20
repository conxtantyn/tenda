package com.tenda.database.repository

import com.tenda.database.mock.FakePersistence
import com.tenda.persistence.core.repository.PersistenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import uniffi.database.Persistence
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PersistenceRepositoryDelegateTest {
    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private lateinit var persistence: Persistence

    private lateinit var repository: PersistenceRepository

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        persistence = Persistence()
        repository = PersistenceRepositoryDelegate(Json, persistence)
    }

    @AfterTest
    fun tearDown() {
        repository.close()
        Dispatchers.resetMain()
    }

    @Test
    fun `test database connection`() = testScope.runTest {
        val result = try {
            repository.open(
                url = "http://<test-token>",
                token = "<test-token>",
                database = ":memory:"
            )
        } catch (_: Throwable) {
            null
        }
        assertEquals(result, Unit)
    }

    @Test
    fun `test execute query`() = testScope.runTest {
        val query = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                age INTEGER,
                score REAL
            )
        """.trimIndent()
        repository.open(
            url = "http://<test-token>",
            token = "<test-token>",
            database = ":memory:"
        )
        val result = repository.execute(query)
        assertEquals(result.isEmpty(), false)
    }

    @Test
    fun `test execute select query`() = testScope.runTest {
        repository.open(
            url = "http://<test-token>",
            token = "<test-token>",
            database = ":memory:"
        )
        val createTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                age INTEGER,
                score REAL
            )
        """.trimIndent()
        val profile = Profile(
            name = "Alice",
            age = 30,
            score = 95.5
        )
        repository.execute(createTable)

        // Insert test data
        val insert = "INSERT INTO users (name, age, score) VALUES (?, ?, ?)"
        repository.execute(insert, listOf(
            profile.name,
            profile.age,
            profile.score
        ))

        // Run SELECT query
        val selectQuery = "SELECT * FROM users ORDER BY id"
        val selectResult = repository.execute(selectQuery, Profile::class)
        val selectedProfile = selectResult.data.first()

        assertNotEquals(selectedProfile.id, null)
        assertEquals(selectedProfile.name, profile.name)
        assertEquals(selectedProfile.age, profile.age)
        assertEquals(selectedProfile.score, profile.score)
    }

    @Test
    fun `test synchronise database`() = testScope.runTest {
        val persistence = FakePersistence
        val repository = PersistenceRepositoryDelegate(Json, persistence)
        repository.open(
            url = "http://<test-token>",
            token = "<test-token>",
            database = ":memory:"
        )
        repository.synchronise()
        assertEquals(persistence.isSynchronized, true)
    }

    @Test
    fun `test close database`() = testScope.runTest {
        val persistence = FakePersistence
        val repository = PersistenceRepositoryDelegate(Json, persistence)
        repository.open(
            url = "http://<test-token>",
            token = "<test-token>",
            database = ":memory:"
        )
        repository.close()
        assertEquals(persistence.isDisconnected, true)
    }

    @Serializable
    class Profile(
        val id: Int? = null,
        val name: String,
        val age: Int,
        val score: Double
    )
}
