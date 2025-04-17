package com.example.fooddream.unitTests.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.models.Ingredient
import com.example.fooddream.repositories.IngredientRepository
import com.google.gson.Gson
import io.mockk.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class IngredientRepoUnitTest {

    private lateinit var mockActivity: AppCompatActivity
    private lateinit var mockPrefs: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor
    private lateinit var repository: IngredientRepository

    private val gson = Gson()
    private val productId = 123
    private val dummyIngredients = listOf(
        Ingredient("Cheese", 50, 1.00, "Cheddar", 1),
        Ingredient("Tomato", 30, 2.00, "Cherry", 2)
    )

    @Before
    fun setup() {
        mockActivity = mockk()
        mockPrefs = mockk()
        mockEditor = mockk()

        every { mockActivity.getSharedPreferences("ingredient_prefs", any()) } returns mockPrefs
        every { mockPrefs.edit() } returns mockEditor
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.remove(any()) } returns mockEditor
        every { mockEditor.apply() } just Runs

        repository = IngredientRepository(mockActivity)
    }

    @Test
    fun `saveIngredients should store JSON in SharedPreferences`() {
        repository.saveIngredients(productId, dummyIngredients)

        val expectedJson = gson.toJson(dummyIngredients)
        verify {
            mockEditor.putString("ingredients_$productId", expectedJson)
            mockEditor.apply()
        }
    }

    @Test
    fun `getIngredients should return deserialized list`() {
        val json = gson.toJson(dummyIngredients)
        every { mockPrefs.getString("ingredients_$productId", null) } returns json

        val result = repository.getIngredients(productId)

        assertEquals(2, result.size)
        assertEquals("Cheese", result[0].getIngredientName())
        assertEquals(1.00, result[0].getWeight())
        assertEquals("Tomato", result[1].getIngredientName())
        assertEquals(2.00, result[1].getWeight())
    }

    @Test
    fun `getIngredients should return empty list if not found`() {
        every { mockPrefs.getString("ingredients_$productId", null) } returns null

        val result = repository.getIngredients(productId)

        assertEquals(emptyList<Ingredient>(), result)
    }

    @Test
    fun `removeIngredient should remove entry from SharedPreferences`() {
        repository.removeIngredient(productId)

        verify {
            mockEditor.remove("ingredients_$productId")
            mockEditor.apply()
        }
    }
}
