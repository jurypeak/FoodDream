package com.example.fooddream.unitTests.model

import com.example.fooddream.models.Account
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals


class AccountUnitTest {

    private lateinit var account: Account

    @BeforeTest
    fun setUp() {
        account = Account(
            email = "test@example.com",
            accountId = 1,
            accessLevel = 0,
            password = "password123"
        )
    }

    @Test
    fun `getEmail should return correct email`() {
        assertEquals("test@example.com", account.getEmail())
    }

    @Test
    fun `setEmail should update email`() {
        account.setEmail("new@example.com")
        assertEquals("new@example.com", account.getEmail())
    }

    @Test
    fun `getAccountId should return correct ID`() {
        assertEquals(1, account.getAccountId())
    }

    @Test
    fun `setAccountId should update ID`() {
        account.setAccountId(42)
        assertEquals(42, account.getAccountId())
    }

    @Test
    fun `getAccessLevel should return correct level`() {
        assertEquals(0, account.getAccessLevel())
    }

    @Test
    fun `setAccessLevel should update level`() {
        account.setAccessLevel(3)
        assertEquals(3, account.getAccessLevel())
    }

    @Test
    fun `getPassword should return correct password`() {
        assertEquals("password123", account.getPassword())
    }

    @Test
    fun `setPassword should update password`() {
        account.setPassword("newpass")
        assertEquals("newpass", account.getPassword())
    }
}
