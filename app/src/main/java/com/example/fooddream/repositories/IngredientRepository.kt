package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.models.Ingredient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

/**
 * IngredientRepository is responsible for managing ingredient data in the application.
 * It provides methods to save, retrieve, and remove ingredients using SharedPreferences.
 *
 * @param view The activity context used for SharedPreferences operations.
 */
class IngredientRepository (private var view: AppCompatActivity){

    /**
     * SharedPreferences instance to store ingredient data.
     * This instance is used to save and retrieve ingredients using JSON serialization.
     */
    private val sharedPreferences: SharedPreferences = view.getSharedPreferences("ingredient_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * Saves a list of ingredients for a specific product ID.
     * The ingredients are serialized to JSON and stored in SharedPreferences.
     *
     * @param productId The ID of the product associated with the ingredients.
     * @param ingredients The list of ingredients to be saved.
     *
     * @throws Exception if an error occurs while saving the ingredients.
     */
    fun saveIngredients(productId: Int, ingredients: List<Ingredient>) {
        try {
            val ingredientsJson = gson.toJson(ingredients)
            sharedPreferences.edit() {
                putString("ingredients_$productId", ingredientsJson)
            }
        } catch (e: Exception) {
            Log.e("IngredientRepository", "Error saving ingredients: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Retrieves a list of ingredients for a specific product ID.
     * The ingredients are deserialized from JSON stored in SharedPreferences.
     *
     * @param productId The ID of the product associated with the ingredients.
     * @return The retrieved list of ingredients, or an empty list if not found.
     *
     * @throws Exception if an error occurs while retrieving the ingredients.
     */
    fun getIngredients(productId: Int): List<Ingredient> {
        try {
            val ingredientsJson = sharedPreferences.getString("ingredients_$productId", null)
            return if (ingredientsJson != null) {
                val type = object : TypeToken<List<Ingredient>>() {}.type
                gson.fromJson(ingredientsJson, type) ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("IngredientRepository", "Error retrieving ingredients: ${e.message}")
            e.printStackTrace()
            return emptyList()
        }
    }

    /**
     * Removes the ingredients for a specific product ID from SharedPreferences.
     *
     * @param productId The ID of the product associated with the ingredients to be removed.
     *
     * @throws Exception if an error occurs while removing the ingredients.
     */
    fun removeIngredient(productId: Int) {
        try {
            sharedPreferences.edit() {
                remove("ingredients_$productId")
            }
        } catch (e: Exception) {
            Log.e("IngredientRepository", "Error removing ingredients: ${e.message}")
            e.printStackTrace()
        }
    }
}