package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.fooddream.models.Order
import com.google.gson.Gson

/**
 * OrderRepository is responsible for managing order data in the application.
 * All orders are of the user logged and are stored on the the users device.
 * It provides methods to save, retrieve, and remove orders using SharedPreferences.
 *
 * @param view The activity context used for SharedPreferences operations.
 */
class OrderRepository(private var view: AppCompatActivity) {

    /**
     * SharedPreferences instance to store order data.
     * This instance is used to save and retrieve orders using JSON serialization.
     */
    private val sharedPreferences: SharedPreferences = view.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * Saves an order object to SharedPreferences.
     * The order object is serialized to JSON and stored in SharedPreferences.
     *
     * @param accountId The ID of the account associated with the order.
     * @param order The order object to be saved.
     *
     * @throws Exception if an error occurs while saving the order.
     */
    fun saveOrder(accountId: Int, order: Order) {
        try {
            val orderJson = gson.toJson(order)
            sharedPreferences.edit {
                putString("order_${accountId}_${order.getOrderId()}", orderJson)
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error saving order: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Retrieves an order object from SharedPreferences.
     * The order object is deserialized from JSON stored in SharedPreferences.
     *
     * @param orderId The ID of the order to be retrieved.
     * @param accountId The ID of the account associated with the order.
     * @return The retrieved order object, or null if not found.
     *
     * @throws Exception if an error occurs while retrieving the order.
     */
    fun getOrder(orderId: Int, accountId: Int): Order? {
        try {
            val orderJson = sharedPreferences.getString("order_${accountId}_${orderId}", null)
            return if (orderJson != null) {
                gson.fromJson(orderJson, Order::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error retrieving order: ${e.message}")
            e.printStackTrace()
            return null
        }
    }

    /**
     * Retrieves all orders.
     * The orders are deserialized from JSON stored in SharedPreferences.
     *
     * @return A list of all orders, or an empty list if none found.
     *
     * @throws Exception if an error occurs while retrieving the orders.
     */
    fun getAllOrders(): ArrayList<Order> {
        try {
            val allOrders = ArrayList<Order>()
            val keys = sharedPreferences.all.keys
            for (key in keys) {
                if (key.startsWith("order_")) {
                    val orderJson = sharedPreferences.getString(key, null)
                    if (orderJson != null) {
                        val order = gson.fromJson(orderJson, Order::class.java)
                        allOrders.add(order)
                    }
                }
            }
            return allOrders
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error retrieving all orders: ${e.message}")
            e.printStackTrace()
            return ArrayList()
        }
    }

    /**
     * Retrieves number of all orders.
     * The orders are deserialized from JSON stored in SharedPreferences.
     *
     * @return A list of all orders for the specified account, or an empty list if none found.
     *
     * @throws Exception if an error occurs while retrieving the orders.
     */
    fun numberOfOrders(): Int {
        try {
            val keys = sharedPreferences.all.keys
            var count = 0
            for (key in keys) {
                if (key.startsWith("order_")) {
                    count++
                }
            }
            return count
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error counting orders: ${e.message}")
            e.printStackTrace()
            return 0
        }
    }

    /**
     * Removes an order from SharedPreferences.
     * This method deletes the order data stored in SharedPreferences.
     *
     * @param orderId The ID of the order to be removed.
     *
     * @throws Exception if an error occurs while removing the order.
     */
    fun removeOrder(orderId: Int) {
        try {
            sharedPreferences.edit() {
                remove("order_$orderId")
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error removing order: ${e.message}")
            e.printStackTrace()
        }
    }
}