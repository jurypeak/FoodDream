package com.example.fooddream.models

class Ingredient (
    private var ingredientName: String,
    private var ingredientId: Int,
    private var weight: Double,
    private var allergens: List<String>,
    private var product: Product
) {
    //Getters
    fun getIngredientName(): String = ingredientName
    fun getIngredientId(): Int = ingredientId
    fun getWeight(): Double = weight
    fun getAllergens(): List<String> = allergens
    fun getProduct(): Product = product

    // Setters
    fun setIngredientName(newIngredientName: String) {
        ingredientName = newIngredientName
    }
    fun setIngredientID(newIngredientId: Int) {
        ingredientId = newIngredientId
    }
    fun setWeight(newWeight: Double) {
        weight = newWeight
    }
    fun setAllergens(newAllergens: List<String>) {
        allergens = newAllergens
    }
}