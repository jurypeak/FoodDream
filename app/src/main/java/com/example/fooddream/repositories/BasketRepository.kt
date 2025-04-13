package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.fooddream.models.BasketItem
import com.google.gson.Gson
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * BasketRepository is responsible for managing basket data in the application.
 * It provides methods to save, retrieve, and manipulate basket items using SharedPreferences.
 *
 * @param view The activity context used for SharedPreferences operations.
 */
class BasketRepository(private var view: AppCompatActivity) {

    /**
     * SharedPreferences instance to store basket data.
     * This instance is used to save and retrieve basket items using JSON serialization.
     */
    private val sharedPreferences: SharedPreferences = view.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * Saves a basket item for a specific product ID and account ID.
     * The basket item is serialized to JSON and stored in SharedPreferences.
     *
     * @param basketItem The basket item to be saved.
     * @param accountId The ID of the account associated with the basket item.
     * @param productId The ID of the product associated with the basket item.
     *
     * @throws Exception if an error occurs while saving the basket item.
     */
    fun saveBasketItem(basketItem: BasketItem, accountId: Int?, productId: Int) {
        try {
            val basketItemJson = gson.toJson(basketItem)
            sharedPreferences.edit() {
                putString("basketItem_${accountId}-${productId}", basketItemJson)
            }
        } catch (e: Exception) {
            Log.e("BasketRepository", "Error saving basket item: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Retrieves a basket item for a specific product ID and account ID.
     * The basket item is deserialized from JSON stored in SharedPreferences.
     *
     * @param productId The ID of the product associated with the basket item.
     * @param accountId The ID of the account associated with the basket item.
     * @return The retrieved basket item, or null if not found.
     *
     * @throws Exception if an error occurs while retrieving the basket item.
     */
    fun getBasketItem(productId: Int, accountId: Int?): BasketItem? {
        try {
            val basketItemJson = sharedPreferences.getString("basketItem_${accountId}-${productId}", null)
            return if (basketItemJson != null) {
                gson.fromJson(basketItemJson, BasketItem::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("BasketRepository", "Error retrieving basket item: ${e.message}")
            e.printStackTrace()
            return null
        }
    }

    /**
     * Retrieves the total price of all items in the basket for a specific account ID.
     * The total price is calculated by summing the price of each basket item multiplied by its quantity.
     *
     * @param accountId The ID of the account associated with the basket items.
     * @return The total price of all items in the basket, rounded to two decimal places.
     *
     * @throws Exception if an error occurs while calculating the total price.
     */
    fun getBasketTotalPrice(accountId: Int?): Double {
        try {
            var basketTotalPrice = 0.00
            for (basketItem in getAllBasketItems(accountId)) {
                basketTotalPrice += basketItem.getPrice() * basketItem.getQuantity()
            }
            var roundedTotal = BigDecimal(basketTotalPrice).setScale(2, RoundingMode.HALF_UP).toDouble()
            return roundedTotal
        } catch (e: Exception) {
            Log.e("BasketRepository", "Error calculating basket total price: ${e.message}")
            e.printStackTrace()
            return 0.00
        }
    }

    /**
     * Retrieves the size of the basket for a specific account ID.
     * The size is calculated by counting the number of items in the basket.
     *
     * @param accountId The ID of the account associated with the basket items.
     * @return The size of the basket (number of items).
     *
     * @throws Exception if an error occurs while calculating the basket size.
     */
    fun getBasketSize(accountId: Int?): Int {
        try {
            return getAllBasketItems(accountId).size
        } catch (e: Exception) {
            Log.e("BasketRepository", "Error getting basket size: ${e.message}")
            e.printStackTrace()
            return 0
        }
    }

    /**
     * Retrieves all basket items for a specific account ID.
     * The basket items are deserialized from JSON stored in SharedPreferences.
     *
     * @param accountId The ID of the account associated with the basket items.
     * @return A list of all basket items for the specified account ID.
     *
     * @throws Exception if an error occurs while retrieving all basket items.
     */
    fun getAllBasketItems(accountId: Int?): ArrayList<BasketItem> {
        try {
            val allBasketItems = ArrayList<BasketItem>()
            val keys = sharedPreferences.all.keys
            for (key in keys) {
                if (key.startsWith("basketItem_$accountId-")) {
                    val basketItemJson = sharedPreferences.getString(key, null)
                    if (basketItemJson != null) {
                        val basketItem = gson.fromJson(basketItemJson, BasketItem::class.java)
                        allBasketItems.add(basketItem)
                    }
                }
            }
            return allBasketItems
        } catch (e: Exception) {
            Log.e("BasketRepository", "Error retrieving all basket items: ${e.message}")
            e.printStackTrace()
            return ArrayList()
        }
    }

    /**
     * Updates the quantity of a specific basket item.
     * The quantity is set to the new value provided.
     *
     * @param productId The ID of the product associated with the basket item.
     * @param accountId The ID of the account associated with the basket item.
     * @param newQuantity The new quantity to be set for the basket item.
     *
     * @throws Exception if an error occurs while updating the quantity.
     */
    fun updateQuantity(productId: Int, accountId: Int?, newQuantity: Int) {
        try {
            val basketItem = getBasketItem(productId, accountId)
            if (basketItem != null) {
                basketItem.setQuantity(newQuantity)
                saveBasketItem(basketItem, accountId, productId)
            } else {
                Log.e("BasketItemRepository", "Product not found in basket")
            }
        } catch (e: Exception) {
            Log.e("BasketRepository", "Error updating quantity: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Clears all basket items from SharedPreferences.
     * This effectively empties the entire basket.
     *
     * @throws Exception if an error occurs while clearing the basket.
     */
    fun clearBasket() {
        try {
            sharedPreferences.edit() {
                clear()
            }
        } catch (e: Exception) {
            Log.e("BasketRepository", "Error clearing basket: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Increments the quantity of a specific basket item by 1.
     * The updated quantity is saved back to SharedPreferences.
     *
     * @param productId The ID of the product associated with the basket item.
     * @param accountId The ID of the account associated with the basket item.
     *
     * @throws Exception if an error occurs while incrementing the quantity.
     */
    fun incrementQuantity(productId: Int, accountId: Int?) {
        try {
            val basketItem = getBasketItem(productId, accountId)
            if (basketItem != null) {
                basketItem.setQuantity(basketItem.getQuantity() + 1)
                saveBasketItem(basketItem, accountId, productId)
            } else {
                Log.e("BasketItemRepository", "Product not found in basket")
            }
        } catch (e: Exception) {
            Log.e("BasketRepository", "Error incrementing quantity: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Decrements the quantity of a specific basket item by 1.
     * The updated quantity is saved back to SharedPreferences.
     *
     * @param productId The ID of the product associated with the basket item.
     * @param accountId The ID of the account associated with the basket item.
     *
     * @throws Exception if an error occurs while decrementing the quantity.
     */
    fun decrementQuantity(productId: Int, accountId: Int?) {
        try {
            val basketItem = getBasketItem(productId, accountId)
            if (basketItem != null) {
                basketItem.setQuantity(basketItem.getQuantity() - 1)
                saveBasketItem(basketItem, accountId, productId)
            } else {
                Log.e("BasketItemRepository", "Product not found in basket or quantity is already 1")
            }
        } catch (e: Exception) {
            Log.e("BasketRepository", "Error decrementing quantity: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Removes a specific basket item from SharedPreferences.
     * The item is identified by its product ID and account ID.
     *
     * @param productId The ID of the product associated with the basket item.
     * @param accountId The ID of the account associated with the basket item.
     *
     * @throws Exception if an error occurs while removing the basket item.
     */
    fun removeBasketItem(productId: Int, accountId: Int?) {
        try {
            sharedPreferences.edit() {
                remove("basketItem_${accountId}-${productId}")
            }
        } catch (e: Exception) {
            Log.e("BasketRepository", "Error removing basket item: ${e.message}")
            e.printStackTrace()
        }
    }
}