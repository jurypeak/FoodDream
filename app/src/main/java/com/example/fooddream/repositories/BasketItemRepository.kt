package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.BasketItem
import com.google.gson.Gson

class BasketItemRepository(private var view: AppCompatActivity) {

    private var notification = Notification()
    private val sharedPreferences: SharedPreferences = view.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveBasketItem(basketItem: BasketItem) {
        val basketItemJson = gson.toJson(basketItem)
        sharedPreferences.edit() {
            putString("basketItem_${basketItem.getProductId()}", basketItemJson)
        }
    }

    fun getBasketItem(productId: Int): BasketItem? {
        val basketItemJson = sharedPreferences.getString("basketItem_${productId}", null)
        return if (basketItemJson != null) {
            gson.fromJson(basketItemJson, BasketItem::class.java)
        } else {
            null
        }
    }

    fun getAllBasketItems(): ArrayList<BasketItem> {
        val allBasketItems = ArrayList<BasketItem>()
        val keys = sharedPreferences.all.keys
        for (key in keys) {
            if (key.startsWith("basketItem_")) {
                val basketItemJson = sharedPreferences.getString(key, null)
                if (basketItemJson != null) {
                    val basketItem = gson.fromJson(basketItemJson, BasketItem::class.java)
                    allBasketItems.add(basketItem)
                }
            }
        }
        return allBasketItems
    }

    fun updateQuantity(productId: Int, newQuantity: Int) {
        val basketItem = getBasketItem(productId)
        if (basketItem != null) {
            basketItem.setQuantity(newQuantity)
            saveBasketItem(basketItem)
        } else {
            Log.e("BasketItemRepository", "Product not found in basket")
        }
    }

    fun incrementQuantity(productId: Int) {
        val basketItem = getBasketItem(productId)
        if (basketItem != null) {
            basketItem.setQuantity(basketItem.getQuantity() + 1)
            saveBasketItem(basketItem)
        } else {
            Log.e("BasketItemRepository", "Product not found in basket")
        }
    }

    fun decrementQuantity(productId: Int) {
        val basketItem = getBasketItem(productId)
        if (basketItem != null) {
            basketItem.setQuantity(basketItem.getQuantity() - 1)
            saveBasketItem(basketItem)
        } else {
            Log.e("BasketItemRepository", "Product not found in basket")
        }
    }

    fun removeBasketItem(productId: Int) {
        sharedPreferences.edit() {
            remove("basketItem_$productId")
        }
    }
}