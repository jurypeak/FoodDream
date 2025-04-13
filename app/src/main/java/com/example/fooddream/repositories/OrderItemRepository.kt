package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.fooddream.models.OrderItem
import com.google.gson.Gson

/**
 * OrderItemRepository is responsible for managing order item data in the application.
 * It provides methods to save, retrieve, and remove order items using SharedPreferences.
 *
 * @param view The activity context used for SharedPreferences operations.
 */
class OrderItemRepository (private var view: AppCompatActivity){

    /**
     * SharedPreferences instance to store order item data.
     * This instance is used to save and retrieve order items using JSON serialization.
     */
    private val sharedPreferences: SharedPreferences = view.getSharedPreferences("orderItem_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * Saves a list of order items for a specific order ID.
     * The order items are serialized to JSON and stored in SharedPreferences.
     *
     * @param orderId The ID of the order associated with the order items.
     * @param orderItems The list of order items to be saved.
     *
     * @throws Exception if an error occurs while saving the order items.
     */
    fun saveOrderItem(orderId: Int, orderItems: ArrayList<OrderItem>) {
        try {
            val orderItemJson = gson.toJson(orderItems)
            sharedPreferences.edit() {
                putString("orderItem_$orderId", orderItemJson)
            }
        } catch (e: Exception) {
            Log.e("OrderItemRepository", "Error saving order item: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Retrieves order item for a specific order ID.
     * The order items are deserialized from JSON stored in SharedPreferences.
     *
     * @param orderId The ID of the order associated with the order item.
     * @return The retrieved list (contains multiple attributes) of order item, or an empty list if not found.
     *
     * @throws Exception if an error occurs while retrieving the order items.
     */
    fun getOrderItem(orderId: Int): List<OrderItem> {
        try {
            val orderItemJson = sharedPreferences.getString("orderItem_$orderId", null)
            return if (orderItemJson != null) {
                val type = object : com.google.gson.reflect.TypeToken<List<OrderItem>>() {}.type
                gson.fromJson(orderItemJson, type)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("OrderItemRepository", "Error retrieving order item: ${e.message}")
            e.printStackTrace()
            return emptyList()
        }
    }

    /**
     * Retrieves all order items for a specific order ID.
     * The order items are deserialized from JSON stored in SharedPreferences.
     *
     * @param orderId The ID of the order associated with the order items.
     * @return The retrieved list of all order items, or an empty list if not found.
     *
     * @throws Exception if an error occurs while retrieving the order items.
     */
    fun getOrderItems(orderId: Int): ArrayList<OrderItem> {
        try {
            val allOrderItems = ArrayList<OrderItem>()
            val type = object : com.google.gson.reflect.TypeToken<List<OrderItem>>() {}.type

            for ((key, value) in sharedPreferences.all) {
                if (key.startsWith("orderItem_${orderId}")) {
                    val orderItemJson = value as? String
                    if (orderItemJson != null) {
                        val items: List<OrderItem> = gson.fromJson(orderItemJson, type)
                        allOrderItems.addAll(items)
                    }
                }
            }
            return allOrderItems
        } catch (e: Exception) {
            Log.e("OrderItemRepository", "Error retrieving all order items: ${e.message}")
            e.printStackTrace()
            return ArrayList()
        }
    }
}