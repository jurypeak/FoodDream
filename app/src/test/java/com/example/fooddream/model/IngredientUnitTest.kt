package com.example.fooddream.model

import com.example.fooddream.models.Ingredient
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class IngredientUnitTest {

    private lateinit var ingredient: Ingredient

    @BeforeTest
    fun setUp() {
        ingredient = Ingredient(
            ingredientName = "Tomato",
            ingredientId = 101,
            weight = 150.0,
            allergens = "None",
            productId = 501
        )
    }

    @Test
    fun `getIngredientName should return correct name`() {
        assertEquals("Tomato", ingredient.getIngredientName())
    }

    @Test
    fun `setIngredientName should update name`() {
        ingredient.setIngredientName("Lettuce")
        assertEquals("Lettuce", ingredient.getIngredientName())
    }

    @Test
    fun `getIngredientId should return correct id`() {
        assertEquals(101, ingredient.getIngredientId())
    }

    @Test
    fun `setIngredientId should update id`() {
        ingredient.setIngredientId(202)
        assertEquals(202, ingredient.getIngredientId())
    }

    @Test
    fun `getWeight should return correct weight`() {
        assertEquals(150.0, ingredient.getWeight())
    }

    @Test
    fun `setWeight should update weight`() {
        ingredient.setWeight(200.5)
        assertEquals(200.5, ingredient.getWeight())
    }

    @Test
    fun `getAllergens should return correct allergen info`() {
        assertEquals("None", ingredient.getAllergens())
    }

    @Test
    fun `setAllergens should update allergen info`() {
        ingredient.setAllergens("Nuts")
        assertEquals("Nuts", ingredient.getAllergens())
    }

    @Test
    fun `getProductId should return correct product id`() {
        assertEquals(501, ingredient.getProductId())
    }

    @Test
    fun `setProductId should update product id`() {
        ingredient.setProductId(600)
        assertEquals(600, ingredient.getProductId())
    }
}
