package com.example.fooddream.controllers

import com.example.fooddream.messengers.Errors
import com.example.fooddream.models.Ingredient
import com.example.fooddream.models.Product

class ProductController (
    private var product: Product
) {
    fun updateProduct() {

    }
    fun addIngredient(ingredient: Ingredient) {
        try {
            product.addIngredients(ingredient)
        } catch (error: Errors.IngredientAdditionException) {

        }
    }
}