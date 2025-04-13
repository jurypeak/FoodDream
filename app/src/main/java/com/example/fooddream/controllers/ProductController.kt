package com.example.fooddream.controllers

import com.example.fooddream.repositories.ProductRepository
import android.util.Log
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Ingredient
import com.example.fooddream.models.Product
import com.example.fooddream.repositories.IngredientRepository
import com.example.fooddream.views.AdminCatalogView
import org.json.JSONException
import org.json.JSONObject
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
    fun addProduct(
        imageURL: String,
        productName: String,
        productDescription: String,
        productPrice: Double,
        productCategory: String,
        productStock: Int,
        productCO: String,
        ingredients: ArrayList<Ingredient>?,
        urlAddProduct: String,
        urlAddIngredient: String,
        requestQueue: RequestQueue,
        notification: Notification,
    ) {
        try {
            val jsonObject = JSONObject().apply {
                put("name", productName)
                put("description", productDescription)
                put("price", productPrice)
                put("category", productCategory)
                put("stock", productStock)
                put("co", productCO)
                put("image", imageURL)
            }
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, urlAddProduct, jsonObject,
                { response ->
                    val returnedResponseStatus = response.optString("status", "")
                    if (returnedResponseStatus == "Success") {
                        notification.sendNotification("Product added successfully", view)
                        val productId = response.optInt("id", -1)
                        addIngredient(productId, ingredients, urlAddIngredient, requestQueue, notification)
                        Log.d("Response", "$response")
                    } else {
                        notification.sendNotification("${response.optString("message", "")}", view)
                        Log.d("Response", "$response")
                    }
                },
                { error ->
                    notification.sendNotification(error.toString(), view)
                    val statusCode = error.networkResponse?.statusCode
                    val errorBody = String(error.networkResponse?.data ?: byteArrayOf())
                    Log.d("Volley Error", "Status Code: $statusCode")
                    Log.d("Volley Error", "Error Body: $errorBody")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: kotlin.Exception) {
            Log.d("Add Product Error", "$error")
        }
    }
    fun addIngredient(
        productId: Int,
        ingredients: ArrayList<Ingredient>?,
        url: String,
        requestQueue: RequestQueue,
        notification: Notification
    ) {
        if (ingredients == null || ingredients.isEmpty()) {
            Log.e("Add Ingredient Error", "No ingredients to add.")
            return
        }
        else {
            for (ingredient in ingredients) {
                try {
                    val jsonObject = JSONObject().apply {
                        put("productId", productId)
                        put("name", ingredient.getIngredientName())
                        put("weight", ingredient.getWeight())
                        put("allergens", ingredient.getAllergens())
                    }
                    val jsonObjectRequest = JsonObjectRequest(
                        Request.Method.POST, url, jsonObject,
                        { response ->
                            val returnedResponseStatus = response.optString("status", "")
                            if (returnedResponseStatus == "Success") {
                                notification.sendNotification("Ingredient added successfully", view)
                                Log.d("Response", "$response")
                            } else {
                                notification.sendNotification("${response.optString("message", "")}", view)
                                Log.d("Response", "$response")
                            }
                        },
                        { error ->
                            notification.sendNotification(error.toString(), view)
                            val statusCode = error.networkResponse?.statusCode
                            val errorBody = String(error.networkResponse?.data ?: byteArrayOf())
                            Log.d("Volley Error", "Status Code: $statusCode")
                            Log.d("Volley Error", "Error Body: $errorBody")
                        })
                    requestQueue.add(jsonObjectRequest)
                } catch (error: kotlin.Exception) {
                    notification.sendNotification(error.toString(), view)
                    Log.d("Add Product Error", "$error")
                }
            }
        }
    }

    fun collectIngredients(ingredientTable: TableLayout): ArrayList<Ingredient>? {
        try {
            val ingredients = ArrayList<Ingredient>()
            for (i in 1 until ingredientTable.childCount) {
                val row = ingredientTable.getChildAt(i) as TableRow
                val name = (row.getChildAt(0) as EditText).text.toString()
                val weight = (row.getChildAt(1) as EditText).text.toString()
                val allergens = (row.getChildAt(2) as EditText).text.toString()

                if (name.isNotEmpty() && weight.isNotEmpty() && allergens.isNotEmpty()) {
                    val ingredient = Ingredient(
                        name,
                        0, // ID is auto-generated
                        weight.toDouble(),
                        allergens,
                        0 // product ID is not known at this point until the product is created
                    )
                    ingredients.add(ingredient)
                } else {
                    Log.e("Ingredient Collection Error", "Name or weight is empty.")
                    return null
                }
            }

            return ingredients
        } catch (error: kotlin.Exception) {
            Log.e("Ingredient Collection Error", "$error")
            return null
        }
    }

    fun removeProduct(
        productId: Int,
        url: String,
        requestQueue: RequestQueue,
        notification: Notification,
        navigationController: NavigationController
    ){
        try {
            val jsonObject = JSONObject().apply {
                put("productId", productId)
            }
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    val returnedResponseStatus = response.optString("status", "")
                    if (returnedResponseStatus == "Success") {
                        notification.sendNotification("Product deleted successfully", view)
                        navigationController.navigateToActivity(AdminCatalogView::class.java)
                        Log.d("Response", "$response")
                    } else {
                        notification.sendNotification("${response.optString("message", "")}", view)
                        Log.d("Response", "$response")
                    }
                },
                { error ->
                    notification.sendNotification(error.toString(), view)
                    val statusCode = error.networkResponse?.statusCode
                    val errorBody = String(error.networkResponse?.data ?: byteArrayOf())
                    Log.d("Volley Error", "Status Code: $statusCode")
                    Log.d("Volley Error", "Error Body: $errorBody")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: kotlin.Exception) {
            Log.d("Add Product Error", "$error")
        }
    }

    fun editProduct(
        productId: Int,
        imageURL: String,
        productName: String,
        productDescription: String,
        productPrice: Double,
        productCategory: String,
        productStock: Int,
        productCO: String,
        ingredients: ArrayList<Ingredient>?,
        urlAddProduct: String,
        urlAddIngredient: String,
        requestQueue: RequestQueue,
        notification: Notification,
    ) {
        try {
            val jsonObject = JSONObject().apply {
                put("id", productId)
                put("name", productName)
                put("description", productDescription)
                put("price", productPrice)
                put("category", productCategory)
                put("stock", productStock)
                put("co", productCO)
                put("image", imageURL)
            }
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, urlAddProduct, jsonObject,
                { response ->
                    val returnedResponseStatus = response.optString("status", "")
                    if (returnedResponseStatus == "Success") {
                        notification.sendNotification("Product edited successfully", view)
                        val fixedIngredients = findIdByIngredientName(ingredients, productId)
                        editIngredient(productId, fixedIngredients, urlAddIngredient, requestQueue, notification)
                        Log.d("Response", "$response")
                    } else {
                        notification.sendNotification("${response.optString("message", "")}", view)
                        Log.d("Response", "$response")
                    }
                },
                { error ->
                    notification.sendNotification(error.toString(), view)
                    val statusCode = error.networkResponse?.statusCode
                    val errorBody = String(error.networkResponse?.data ?: byteArrayOf())
                    Log.d("Volley Error", "Status Code: $statusCode")
                    Log.d("Volley Error", "Error Body: $errorBody")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: kotlin.Exception) {
            Log.d("Edit Product Error", "$error")
        }
    }

    fun findIdByIngredientName(
        ingredients: ArrayList<Ingredient>?,
        productId: Int
    ): ArrayList<Ingredient> {
        val updatedIngredients = ArrayList<Ingredient>()
        if (ingredients.isNullOrEmpty()) {
            Log.e("Find ID Error", "No ingredients to find ID.")
            return updatedIngredients
        }
        try {
            val ingredientRepository = IngredientRepository(view)
            val allIngredientsForProduct = ingredientRepository.getIngredients(productId)
            if (allIngredientsForProduct.isNullOrEmpty()) {
                Log.e("Find ID Error", "No ingredients found in the repository for productId: $productId")
                return updatedIngredients
            }
            for (ingredient in ingredients) {
                val ingredientName = ingredient.getIngredientName()

                val matchingIngredient = allIngredientsForProduct.find { it.getIngredientName() == ingredientName }

                if (matchingIngredient != null) {
                    ingredient.setIngredientId(matchingIngredient.getIngredientId())
                    Log.d("Find ID", "Found ID: ${ingredient.getIngredientId()} for ingredient: $ingredientName")
                } else {
                    Log.e("Find ID Error", "No ID found for ingredient: $ingredientName")
                }

                updatedIngredients.add(ingredient)
            }
        } catch (error: Exception) {
            Log.e("Find ID Error", "An error occurred: $error")
        }

        return updatedIngredients
    }


    fun editIngredient(
        productId: Int,
        ingredients: ArrayList<Ingredient>?,
        url: String,
        requestQueue: RequestQueue,
        notification: Notification
    ) {
        if (ingredients == null || ingredients.isEmpty()) {
            Log.e("Edit Ingredient Error", "No ingredients to edit.")
            return
        } else {
            for (ingredient in ingredients) {
                try {
                    val jsonObject = JSONObject().apply {
                        put("ingredientId", ingredient.getIngredientId())
                        put("productId", productId)
                        put("name", ingredient.getIngredientName())
                        put("weight", ingredient.getWeight())
                        put("allergens", ingredient.getAllergens())
                    }
                    val jsonObjectRequest = JsonObjectRequest(
                        Request.Method.POST, url, jsonObject,
                        { response ->
                            val returnedResponseStatus = response.optString("status", "")
                            if (returnedResponseStatus == "Success") {
                                notification.sendNotification("Ingredient edited successfully", view)
                                Log.d("Response", "$response")
                            } else {
                                notification.sendNotification(
                                    "${
                                        response.optString(
                                            "message",
                                            ""
                                        )
                                    }", view
                                )
                                Log.d("Response", "$response")
                            }
                        },
                        { error ->
                            notification.sendNotification(error.toString(), view)
                            val statusCode = error.networkResponse?.statusCode
                            val errorBody = String(error.networkResponse?.data ?: byteArrayOf())
                            Log.d("Volley Error", "Status Code: $statusCode")
                            Log.d("Volley Error", "Error Body: $errorBody")
                        })
                    requestQueue.add(jsonObjectRequest)
                } catch (error: kotlin.Exception) {
                    notification.sendNotification(error.toString(), view)
                    Log.d("Edit Product Error", "$error")
                }
            }
        }
    }
}