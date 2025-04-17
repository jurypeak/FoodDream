package com.example.fooddream.unitTests.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.models.Product
import com.example.fooddream.repositories.ProductRepository
import com.google.gson.Gson
import io.mockk.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ProductRepoUnitTest {

    private lateinit var mockActivity: AppCompatActivity
    private lateinit var mockPrefs: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor
    private lateinit var repository: ProductRepository
    private val gson = Gson()

    private val product = Product("Cheeseburger", 1, 8.99, "UK", 2, "Delicious", "Food", "")
    private val productJson = gson.toJson(product)

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

        repository = ProductRepository(mockActivity)
    }

    @Test
    fun `saveProduct should serialize and store in SharedPreferences`() {
        repository.saveProduct(product)

        verify {
            mockEditor.putString("product_1", productJson)
            mockEditor.apply()
        }
    }

    @Test
    fun `getProduct should return a Product object when found`() {
        every { mockPrefs.getString("product_1", null) } returns productJson

        val result = repository.getProduct(1)

        assertNotNull(result)
        assertEquals("Cheeseburger", result.getProductName())
        assertEquals(8.99, result.getProductPrice())
    }

    @Test
    fun `getProduct should return null when not found`() {
        every { mockPrefs.getString("product_1", null) } returns null

        val result = repository.getProduct(1)

        assertNull(result)
    }

    @Test
    fun `getAllProducts should return all deserialized Product objects`() {
        val mockMap = mapOf(
            "product_1" to productJson,
            "product_2" to gson.toJson(Product("Fries", 1, 8.99, "UK", 2, "Delicious", "Food", ""))
        )

        every { mockPrefs.all } returns mockMap
        every { mockPrefs.getString(any(), null) } answers { mockMap[firstArg()] }

        val result = repository.getAllProducts()

        assertEquals(2, result.size)
        assertEquals("Cheeseburger", result[0].getProductName())
        assertEquals("Fries", result[1].getProductName())
    }

    @Test
    fun `getAllProducts should return empty list if no product keys exist`() {
        every { mockPrefs.all } returns mapOf("nonproduct_key" to "value")

        val result = repository.getAllProducts()

        assertEquals(0, result.size)
    }

    @Test
    fun `removeProduct should delete product from SharedPreferences`() {
        repository.removeProduct(1)

        verify {
            mockEditor.remove("product_1")
            mockEditor.apply()
        }
    }
}
