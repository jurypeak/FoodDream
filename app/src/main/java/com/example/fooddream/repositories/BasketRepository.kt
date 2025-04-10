package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.BasketItem
import com.google.gson.Gson
import java.math.BigDecimal
import java.math.RoundingMode

class BasketRepository(private var view: AppCompatActivity) {

    private var notification = Notification()
    private val sharedPreferences: SharedPreferences = view.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveBasketItem(basketItem: BasketItem, accountId: Int?, productId: Int) {
        val basketItemJson = gson.toJson(basketItem)
        sharedPreferences.edit() {
            putString("basketItem_${accountId}-${productId}", basketItemJson)
        }
    }

    fun getBasketItem(productId: Int, accountId: Int?): BasketItem? {
        val basketItemJson = sharedPreferences.getString("basketItem_${accountId}-${productId}", null)
        return if (basketItemJson != null) {
            gson.fromJson(basketItemJson, BasketItem::class.java)
        } else {
            null
        }
    }

    fun getBasketTotalPrice(accountId: Int?): Double {
        var basketTotalPrice = 0.00
        for (basketItem in getAllBasketItems(accountId)) {
            basketTotalPrice += basketItem.getPrice() * basketItem.getQuantity()
        }
        var roundedTotal = BigDecimal(basketTotalPrice).setScale(2, RoundingMode.HALF_UP).toDouble()
        return roundedTotal
    }

    fun getBasketSize(accountId: Int?): Int {
        return getAllBasketItems(accountId).size
    }

    fun getAllBasketItems(accountId: Int?): ArrayList<BasketItem> {
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
    }

    fun updateQuantity(productId: Int, accountId: Int?, newQuantity: Int) {
        val basketItem = getBasketItem(productId, accountId)
        if (basketItem != null) {
            basketItem.setQuantity(newQuantity)
            saveBasketItem(basketItem, accountId, productId)
        } else {
            Log.e("BasketItemRepository", "Product not found in basket")
        }
    }

    fun clearBasket() {
        sharedPreferences.edit() {
            clear()
        }
    }

    fun incrementQuantity(productId: Int, accountId: Int?) {
        val basketItem = getBasketItem(productId, accountId)
        if (basketItem != null) {
            basketItem.setQuantity(basketItem.getQuantity() + 1)
            saveBasketItem(basketItem, accountId, productId)
        } else {
            Log.e("BasketItemRepository", "Product not found in basket")
        }
    }

    fun decrementQuantity(productId: Int, accountId: Int?) {
        val basketItem = getBasketItem(productId, accountId)
        if (basketItem != null) {
            basketItem.setQuantity(basketItem.getQuantity() - 1)
            saveBasketItem(basketItem, accountId, productId)
        } else {
            Log.e("BasketItemRepository", "Product not found in basket")
        }
    }

    fun removeBasketItem(productId: Int, accountId: Int?) {
        sharedPreferences.edit() {
            remove("basketItem_$accountId-$productId")
        }
    }
}