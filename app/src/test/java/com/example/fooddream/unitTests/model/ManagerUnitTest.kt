package com.example.fooddream.unitTests.model

import com.example.fooddream.models.Manager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ManagerUnitTest {

    private lateinit var manager: Manager

    @BeforeTest
    fun setUp() {
        manager = Manager(
            email = "manager@example.com",
            accountId = 9001,
            accessLevel = 10,
            password = "adminPass"
        )
    }

    @Test
    fun `getEmail should return correct email`() {
        assertEquals("manager@example.com", manager.getEmail())
    }

    @Test
    fun `setEmail should update email`() {
        manager.setEmail("new.manager@example.com")
        assertEquals("new.manager@example.com", manager.getEmail())
    }

    @Test
    fun `getAccountId should return correct account ID`() {
        assertEquals(9001, manager.getAccountId())
    }

    @Test
    fun `setAccountId should update account ID`() {
        manager.setAccountId(9002)
        assertEquals(9002, manager.getAccountId())
    }

    @Test
    fun `getAccessLevel should return correct access level`() {
        assertEquals(10, manager.getAccessLevel())
    }

    @Test
    fun `setAccessLevel should update access level`() {
        manager.setAccessLevel(5)
        assertEquals(5, manager.getAccessLevel())
    }

    @Test
    fun `getPassword should return correct password`() {
        assertEquals("adminPass", manager.getPassword())
    }

    @Test
    fun `setPassword should update password`() {
        manager.setPassword("newSecurePass")
        assertEquals("newSecurePass", manager.getPassword())
    }
}
