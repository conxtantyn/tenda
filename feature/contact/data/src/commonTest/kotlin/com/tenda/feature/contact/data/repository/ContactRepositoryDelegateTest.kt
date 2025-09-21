package com.tenda.feature.contact.data.repository

import com.tenda.feature.contact.data.faker.FakeContactPersistence
import com.tenda.feature.contact.domain.model.Contact
import com.tenda.feature.contact.domain.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ContactRepositoryDelegateTest {
    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private val persistence = FakeContactPersistence()

    private lateinit var repository: ContactRepository

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = ContactRepositoryDelegate(persistence)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get delegates to persistence`() = testScope.runTest {
        val expected = Contact(
            id = "1",
            name = "Alice",
            logo = "https://example.com/alice.jpg"
        )
        persistence.getReturn = expected

        val result = repository.get("1")

        assertEquals(expected, result)
        assertTrue("get:1" in persistence.calledMethods)
    }

    @Test
    fun `create delegates to persistence`() = runTest {
        val expected = Contact(
            id = "2",
            name = "Bob",
            logo = "https://example.com/bob.jpg"
        )
        persistence.createReturn = expected

        val result = repository.create("Bob")

        assertEquals(expected, result)
        assertTrue("create:Bob" in persistence.calledMethods)
    }

    @Test
    fun `delete delegates to persistence`() = runTest {
        repository.delete("3")

        assertTrue("delete:3" in persistence.calledMethods)
    }
}
