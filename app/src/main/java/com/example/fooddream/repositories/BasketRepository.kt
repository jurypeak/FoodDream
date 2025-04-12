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

class BasketRepository(private var view: AppCompatActivity) {

    private val sharedPreferences: SharedPreferences = view.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

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

    fun getBasketSize(accountId: Int?): Int {
        try {
            return getAllBasketItems(accountId).size
        } catch (e: Exception) {
            Log.e("BasketRepository", "Error getting basket size: ${e.message}")
            e.printStackTrace()
            return 0
        }
    }

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