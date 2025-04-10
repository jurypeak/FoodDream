package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.fooddream.models.Payment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PaymentRepository (private var view: AppCompatActivity){
    private val sharedPreferences: SharedPreferences =
        view.getSharedPreferences("payment_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun savePayments(orderId: Int, payments: ArrayList<Payment>) {
        val paymentJson = gson.toJson(payments)
        sharedPreferences.edit() {
            putString("payments_$orderId", paymentJson)
        }
    }

    fun getPayment(orderId: Int): Payment? {
        val paymentJson = sharedPreferences.getString("payments_$orderId", null)
        return if (!paymentJson.isNullOrEmpty()) {
            val type = object : TypeToken<List<Payment>>() {}.type
            val paymentList: List<Payment> = gson.fromJson(paymentJson, type)
            paymentList.firstOrNull()
        } else {
            null
        }
    }

    fun getPayments(): ArrayList<Payment> {
        val allPayments = ArrayList<Payment>()
        val keys = sharedPreferences.all.keys
        for (key in keys) {
            if (key.startsWith("payments_")) {
                val paymentJson = sharedPreferences.getString(key, null)
                if (paymentJson != null) {
                    val type = object : TypeToken<ArrayList<Payment>>() {}.type
                    val payments: ArrayList<Payment> = gson.fromJson(paymentJson, type)
                    allPayments.addAll(payments)
                }
            }
        }
        return allPayments
    }

}