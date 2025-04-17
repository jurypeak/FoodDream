package com.example.fooddream.unitTests.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.models.BasketItem
import com.example.fooddream.repositories.BasketRepository
import com.google.gson.Gson
import io.mockk.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BasketRepoUnitTest {

    private lateinit var repository: BasketRepository
    private lateinit var mockActivity: AppCompatActivity
    private lateinit var mockPrefs: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor

    private val gson = Gson()

    /**
     * Mocking the AppCompatActivity and SharedPreferences
     * to avoid using the actual Android framework
     * and to isolate the unit tests.
     * This allows me to test the BasketRepository
     * https://stackoverflow.com/questions/57150850/test-sharedpreferences-in-android
     */
    @Before
    fun setup() {
        mockActivity = mockk()
        mockPrefs = mockk(relaxed = true)
        mockEditor = mockk(relaxed = true)

        every { mockActivity.getSharedPreferences("app_prefs", any()) } returns mockPrefs
        every { mockPrefs.edit() } returns mockEditor
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.remove(any()) } returns mockEditor
        every { mockEditor.apply() } just Runs

        repository = BasketRepository(mockActivity)
    }

    @Test
    fun `saveBasketItem saves item to SharedPreferences`() {
        val basketItem = BasketItem(
            productId = 1,
            basketId = 1,
            quantity = 2,
            price = 5.99,
            itemName = "Burger"
        )
        val json = gson.toJson(basketItem)

        repository.saveBasketItem(basketItem, 1, 101)

        verify {
            mockEditor.putString("basketItem_1-101", json)
            mockEditor.apply()
        }
    }

    @Test
    fun `getBasketItem returns deserialized BasketItem`() {
        val basketItem = BasketItem(
            productId = 1,
            basketId = 1,
            quantity = 2,
            price = 5.99,
            itemName = "Burger"
        )
        val json = gson.toJson(basketItem)

        every { mockPrefs.getString("basketItem_1-101", null) } returns json

        val result = repository.getBasketItem(101, 1)

        assertEquals(basketItem.getProductId(), result?.getProductId())
        assertEquals(basketItem.getBasketId(), result?.getBasketId())
        assertEquals(basketItem.getQuantity(), result?.getQuantity())
        assertEquals(basketItem.getPrice(), result?.getPrice())
        assertEquals(basketItem.getItemName(), result?.getItemName())
    }

    @Test
    fun `getBasketItem returns null if not found`() {
        every { mockPrefs.getString("basketItem_1-101", null) } returns null

        val result = repository.getBasketItem(101, 1)

        assertNull(result)
    }

    @Test
    fun `getBasketTotalPrice returns total price`() {
        val item1 = BasketItem(
            productId = 1,
            basketId = 1,
            quantity = 2,
            price = 5.99,
            itemName = "Burger"
        )
        val item2 = BasketItem(
            productId = 11,
            basketId = 12,
            quantity = 24,
            price = 2.99,
            itemName = "Fries"
        )
        val json1 = gson.toJson(item1)
        val json2 = gson.toJson(item2)

        val allPrefs = mutableMapOf(
            "basketItem_1-1" to json1,
            "basketItem_1-11" to json2
        )

        every { mockPrefs.all } returns allPrefs
        every { mockPrefs.getString("basketItem_1-1", null) } returns json1
        every { mockPrefs.getString("basketItem_1-11", null) } returns json2

        val total = repository.getBasketTotalPrice(1)
        val expectedTotal = ((2 * 5.99) + (24 * 2.99)).toBigDecimal()
            .setScale(2, java.math.RoundingMode.HALF_EVEN)
            .toDouble()

        assertEquals(expectedTotal, total)
    }

    @Test
    fun `getBasketSize returns correct size`() {
        val item1 = BasketItem(
            productId = 1,
            basketId = 1,
            quantity = 2,
            price = 5.99,
            itemName = "Burger"
        )
        val item2 = BasketItem(
            productId = 11,
            basketId = 12,
            quantity = 24,
            price = 2.99,
            itemName = "Fries"
        )
        val map = mapOf(
            "basketItem_1-1" to gson.toJson(item1),
            "basketItem_1-11" to gson.toJson(item2)
        )

        every { mockPrefs.all } returns map

        val size = repository.getBasketSize(1)

        assertEquals(2, size)
    }

    @Test
    fun `clearBasket removes all basket items`() {
        val keys: MutableSet<String> = mutableSetOf(
            "basketItem_1-1",
            "basketItem_1-11",
            "someOtherKey"
        )

        every { mockPrefs.all.keys } returns keys

        repository.clearBasket()

        verify { mockEditor.remove("basketItem_1-1") }
        verify { mockEditor.remove("basketItem_1-11") }
        verify(exactly = 0) { mockEditor.remove("someOtherKey") }
        verify { mockEditor.apply() }
    }

    @Test
    fun `updateQuantity updates quantity and saves`() {
        val item = BasketItem(
            productId = 1,
            basketId = 1,
            quantity = 2,
            price = 5.99,
            itemName = "Burger"
        )
        val json = gson.toJson(item)

        every { mockPrefs.getString("basketItem_1-1", null) } returns json

        repository.updateQuantity(1, 1, 5)

        verify {
            mockEditor.putString("basketItem_1-1", match { it.contains("\"quantity\":5") })
            mockEditor.apply()
        }
    }

    @Test
    fun `incrementQuantity increases item quantity by 1`() {
        val item = BasketItem(
            productId = 1,
            basketId = 1,
            quantity = 2,
            price = 5.99,
            itemName = "Burger"
        )
        every { mockPrefs.getString("basketItem_1-1", null) } returns gson.toJson(item)

        repository.incrementQuantity(1, 1)

        verify {
            mockEditor.putString("basketItem_1-1", match { it.contains("\"quantity\":3") })
            mockEditor.apply()
        }
    }

    @Test
    fun `decrementQuantity decreases item quantity by 1`() {
        val item = BasketItem(
            productId = 1,
            basketId = 1,
            quantity = 2,
            price = 5.99,
            itemName = "Burger"
        )
        every { mockPrefs.getString("basketItem_1-1", null) } returns gson.toJson(item)

        repository.decrementQuantity(1, 1)

        verify {
            mockEditor.putString("basketItem_1-1", match { it.contains("\"quantity\":1") })
            mockEditor.apply()
        }
    }

    @Test
    fun `removeBasketItem removes correct item`() {
        repository.removeBasketItem(1, 1)

        verify {
            mockEditor.remove("basketItem_1-1")
            mockEditor.apply()
        }
    }
}

