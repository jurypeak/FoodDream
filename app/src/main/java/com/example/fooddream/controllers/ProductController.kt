package com.example.fooddream.controllers

import ProductRepository
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.example.fooddream.messengers.Errors
import com.example.fooddream.models.Ingredient
import com.example.fooddream.models.Product
import com.example.fooddream.repositories.IngredientRepository
import org.json.JSONException
import java.lang.Exception

class ProductController (private val view: AppCompatActivity) {

    fun getProductsInDB(
        requestQueue: RequestQueue,
        url: String,
        searchQuery: String?,
        callback: (List<Product>?) -> Unit
    ) {
        try {
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    try {
                        val productRepository = ProductRepository(view)
                        val ingredientRepository = IngredientRepository(view)
                        val productsList = ArrayList<Product>()

                        for (i in 0 until response.length()) {
                            val productJson = response.getJSONObject(i)

                            val productId = productJson.getInt("id")
                            val productName = productJson.getString("name")
                            val productPrice = productJson.getDouble("price")
                            val productCO = productJson.getString("co")
                            val productStock = productJson.getInt("stock")
                            val productDescription = productJson.getString("description")
                            val productCategory = productJson.getString("category")
                            val productImage = productJson.getString("image")

                            val ingredientsArray = productJson.getJSONArray("ingredients")
                            val ingredients = mutableListOf<Ingredient>()

                            for (j in 0 until ingredientsArray.length()) {
                                val ingredientJson = ingredientsArray.getJSONObject(j)

                                val ingredientId = ingredientJson.getInt("id")
                                val ingredientName = ingredientJson.getString("name")
                                val ingredientWeight = ingredientJson.getDouble("weight")
                                val ingredientAllergens = ingredientJson.getString("allergens")

                                ingredients.add(
                                    Ingredient(
                                        ingredientName,
                                        ingredientId,
                                        ingredientWeight,
                                        ingredientAllergens,
                                        productId
                                    )
                                )
                            }

                            val product = Product(
                                productName,
                                productId,
                                productPrice,
                                productCO,
                                productStock,
                                productDescription,
                                productCategory,
                                productImage,
                            )

                            if (searchQuery == null || productName.contains(searchQuery, ignoreCase = true) || productCategory.contains(searchQuery, ignoreCase = true)) {
                                productsList.add(product)
                                productRepository.saveProduct(product)
                                ingredientRepository.saveIngredients(productId, ingredients)
                            }
                        }

                        callback(productsList)
                    } catch (e: JSONException) {
                        Log.e("Volley Error", "JSON parsing error: $e")
                        callback(null)
                    }
                },
                { error ->
                    Log.e("Volley Error", "Error: ${error.message}")
                    callback(null)
                }
            )

            requestQueue.add(jsonArrayRequest)
        } catch (error: Exception) {
            Log.e("Product Fetch Error", "$error")
            callback(null)
        }
    }


    fun updateProduct() {

    }

    fun addIngredient(product: Product, ingredient: Ingredient) {
        try {

        } catch (error: Errors.IngredientAdditionException) {

        }
    }
}