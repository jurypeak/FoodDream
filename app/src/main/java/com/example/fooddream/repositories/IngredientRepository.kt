package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
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
        val ingredientsJson = gson.toJson(ingredients)
        sharedPreferences.edit() {
            putString("ingredients_$productId", ingredientsJson)
        }
    }

    fun getIngredients(productId: Int): List<Ingredient> {
        val ingredientsJson = sharedPreferences.getString("ingredients_$productId", null)
        return if (!ingredientsJson.isNullOrEmpty()) {
            val type = object : TypeToken<List<Ingredient>>() {}.type
            gson.fromJson(ingredientsJson, type) ?: emptyList()
        } else {
            emptyList()
        }
    }
}