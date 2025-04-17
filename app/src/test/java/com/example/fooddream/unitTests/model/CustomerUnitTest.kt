package com.example.fooddream.unitTests.model

import com.example.fooddream.models.Customer
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CustomerUnitTest {

    private lateinit var customer: Customer

    @BeforeTest
    fun setUp() {
        customer = Customer(
            fName = "Alice",
            lName = "Johnson",
            email = "alice@example.com",
            accountId = 1001,
            accessLevel = 1,
            password = "securePassword123"
        )
    }

    @Test
    fun `getFName should return correct first name`() {
        assertEquals("Alice", customer.getFName())
    }

    @Test
    fun `setFName should update first name`() {
        customer.setFName("Bob")
        assertEquals("Bob", customer.getFName())
    }

    @Test
    fun `getLName should return correct last name`() {
        assertEquals("Johnson", customer.getLName())
    }

    @Test
    fun `setLName should update last name`() {
        customer.setLName("Smith")
        assertEquals("Smith", customer.getLName())
    }

    @Test
    fun `getEmail should return inherited email`() {
        assertEquals("alice@example.com", customer.getEmail())
    }

    @Test
    fun `setEmail should update inherited email`() {
        customer.setEmail("bob@example.com")
        assertEquals("bob@example.com", customer.getEmail())
    }

    @Test
    fun `getAccountId should return inherited account ID`() {
        assertEquals(1001, customer.getAccountId())
    }

    @Test
    fun `setAccountId should update inherited account ID`() {
        customer.setAccountId(2002)
        assertEquals(2002, customer.getAccountId())
    }

    @Test
    fun `getAccessLevel should return inherited access level`() {
        assertEquals(1, customer.getAccessLevel())
    }

    @Test
    fun `setAccessLevel should update inherited access level`() {
        customer.setAccessLevel(2)
        assertEquals(2, customer.getAccessLevel())
    }

    @Test
    fun `getPassword should return inherited password`() {
        assertEquals("securePassword123", customer.getPassword())
    }

    @Test
    fun `setPassword should update inherited password`() {
        customer.setPassword("newPass456")
        assertEquals("newPass456", customer.getPassword())
    }
}
