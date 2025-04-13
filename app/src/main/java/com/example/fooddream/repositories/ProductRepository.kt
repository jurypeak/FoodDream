package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.models.Product
import com.google.gson.Gson
import androidx.core.content.edit

/**
 * ProductRepository is responsible for managing product data in the application.
 * It provides methods to save, retrieve, and remove products using SharedPreferences.
 *
 * @param view The activity context used for SharedPreferences operations.
 */
class ProductRepository(private var view: AppCompatActivity) {

    /**
     * SharedPreferences instance to store product data.
     * This instance is used to save and retrieve products using JSON serialization.
     */
    private val sharedPreferences: SharedPreferences = view.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * Saves a product object to SharedPreferences.
     * The product object is serialized to JSON and stored in SharedPreferences.
     *
     * @param product The product object to be saved.
     *
     * @throws Exception if an error occurs while saving the product.
     */
    fun saveProduct(product: Product) {
        try {
            val productJson = gson.toJson(product)
            sharedPreferences.edit() {
                putString("product_${product.getProductId()}", productJson)
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error saving product: ${e.message}")
        }
    }

    /**
     * Retrieves a product object from SharedPreferences.
     * The product object is deserialized from JSON stored in SharedPreferences.
     *
     * @param productId The ID of the product to be retrieved.
     * @return The retrieved product object, or null if not found.
     *
     * @throws Exception if an error occurs while retrieving the product.
     */
    fun getProduct(productId: Int): Product? {
        try {
            val productJson = sharedPreferences.getString("product_$productId", null)
            return if (productJson != null) {
                gson.fromJson(productJson, Product::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error retrieving product: ${e.message}")
            return null
        }
    }

    /**
     * Retrieves all products stored in SharedPreferences.
     * The products are deserialized from JSON stored in SharedPreferences.
     *
     * @return A list of all retrieved product objects.
     *
     * @throws Exception if an error occurs while retrieving the products.
     */
    fun getAllProducts(): ArrayList<Product> {
        try {
            val allProducts = ArrayList<Product>()
            val keys = sharedPreferences.all.keys
            for (key in keys) {
                if (key.startsWith("product_")) {
                    val productJson = sharedPreferences.getString(key, null)
                    if (productJson != null) {
                        val product = gson.fromJson(productJson, Product::class.java)
                        allProducts.add(product)
                    }
                }
            }
            return allProducts
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error retrieving all products: ${e.message}")
            return ArrayList()
        }
    }

    /**
     * Removes a product from SharedPreferences.
     * The product is identified by its ID and removed from SharedPreferences.
     *
     * @param productId The ID of the product to be removed.
     *
     * @throws Exception if an error occurs while removing the product.
     */
    fun removeProduct(productId: Int) {
        try {
            sharedPreferences.edit() {
                remove("product_$productId")
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error removing product: ${e.message}")
        }
    }
}
