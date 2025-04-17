package com.example.fooddream.unitTests.model

import com.example.fooddream.models.Address
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AddressUnitTest {

    private lateinit var address: Address

    @BeforeTest
    fun setUp() {
        address = Address(
            addressId = 100,
            orderId = 200,
            street = "123 Main St",
            postcode = "PH1492F",
            town = "Springfield"
        )
    }

    @Test
    fun `getAddressId should return correct ID`() {
        assertEquals(100, address.getAddressId())
    }

    @Test
    fun `setAddressId should update addressId`() {
        address.setAddressId(101)
        assertEquals(101, address.getAddressId())
    }

    @Test
    fun `getOrderId should return correct orderId`() {
        assertEquals(200, address.getOrderId())
    }

    @Test
    fun `setOrderId should update orderId`() {
        address.setOrderId(201)
        assertEquals(201, address.getOrderId())
    }

    @Test
    fun `getStreet should return correct street`() {
        assertEquals("123 Main St", address.getStreet())
    }

    @Test
    fun `setStreet should update street`() {
        address.setStreet("456 Elm St")
        assertEquals("456 Elm St", address.getStreet())
    }

    @Test
    fun `getPostcode should return correct postcode`() {
        assertEquals("PH1492F", address.getPostcode())
    }

    @Test
    fun `setPostcode should update postcode`() {
        address.setPostcode("PH1392N")
        assertEquals("PH1392N", address.getPostcode())
    }

    @Test
    fun `getTown should return correct town`() {
        assertEquals("Springfield", address.getTown())
    }

    @Test
    fun `setTown should update town`() {
        address.setTown("Shelbyville")
        assertEquals("Shelbyville", address.getTown())
    }
}
