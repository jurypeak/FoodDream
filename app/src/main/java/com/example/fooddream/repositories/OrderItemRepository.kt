package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.fooddream.models.OrderItem
import com.google.gson.Gson

class OrderItemRepository (private var view: AppCompatActivity){
    private val sharedPreferences: SharedPreferences =
        view.getSharedPreferences("orderItem_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

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