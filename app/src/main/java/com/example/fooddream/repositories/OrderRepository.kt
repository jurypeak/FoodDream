package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.fooddream.models.Order
import com.google.gson.Gson

class OrderRepository(private var view: AppCompatActivity) {
    private val sharedPreferences: SharedPreferences =
        view.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveOrder(accountId: Int, order: Order) {
        val orderJson = gson.toJson(order)
        sharedPreferences.edit {
            putString("order_${accountId}_${order.getOrderId()}", orderJson)
        }
    }

    fun getOrder(orderId: Int): Order? {
        val orderJson = sharedPreferences.getString("order_$orderId", null)
        return if (orderJson != null) {
            gson.fromJson(orderJson, Order::class.java)
        } else {
            null
        }
    }

    fun getAllOrders(): ArrayList<Order> {
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
    }

    fun getOrdersByAccountId(accountId: Int): List<Order> {
        val allOrders = ArrayList<Order>()
        val keys = sharedPreferences.all.keys
        for (key in keys) {
            if (key.startsWith("order_")) {
                val orderJson = sharedPreferences.getString(key, null)
                if (orderJson != null) {
                    val order = gson.fromJson(orderJson, Order::class.java)
                    if (order.getAccountId() == accountId) {
                        allOrders.add(order)
                    }
                }
            }
        }
        return allOrders
    }

    fun numberOfOrders(): Int {
        val keys = sharedPreferences.all.keys
        var count = 0
        for (key in keys) {
            if (key.startsWith("order_")) {
                count++
            }
        }
        return count
    }

    fun removeOrder(orderId: Int) {
        sharedPreferences.edit() {
            remove("order_$orderId")
        }
    }
}