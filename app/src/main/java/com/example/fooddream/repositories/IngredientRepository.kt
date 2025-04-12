package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.models.Ingredient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

class IngredientRepository (private var view: AppCompatActivity){
    private val sharedPreferences: SharedPreferences =
        view.getSharedPreferences("ingredient_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

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