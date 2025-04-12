package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.fooddream.models.Order
import com.google.gson.Gson

class OrderRepository(private var view: AppCompatActivity) {
    private val sharedPreferences: SharedPreferences =
        view.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

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