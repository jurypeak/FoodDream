package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.fooddream.models.Payment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * PaymentRepository is responsible for managing payment data in the application.
 * It provides methods to save, retrieve, and remove payments using SharedPreferences.
 *
 * @param view The activity context used for SharedPreferences operations.
 */
class PaymentRepository (private var view: AppCompatActivity){

    /**
     * SharedPreferences instance to store payment data.
     * This instance is used to save and retrieve payments using JSON serialization.
     */
    private val sharedPreferences: SharedPreferences = view.getSharedPreferences("payment_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * Saves a list of payments for a specific order ID.
     * The payments are serialized to JSON and stored in SharedPreferences.
     *
     * @param orderId The ID of the order associated with the payments.
     * @param payments The list of payments to be saved.
     *
     * @throws Exception if an error occurs while saving the payments.
     */
    fun savePayments(orderId: Int, payments: ArrayList<Payment>) {
        try {
            val paymentJson = gson.toJson(payments)
            sharedPreferences.edit() {
                putString("payments_$orderId", paymentJson)
            }
        } catch (e: Exception) {
            Log.e("PaymentRepository", "Error saving payments: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Retrieves a single payment for a specific order ID.
     * The payment is deserialized from JSON stored in SharedPreferences.
     *
     * @param orderId The ID of the order associated with the payment.
     * @return The retrieved payment, or null if not found.
     *
     * @throws Exception if an error occurs while retrieving the payment.
     */
    fun getPayment(orderId: Int): Payment? {
        try {
            val paymentJson = sharedPreferences.getString("payments_$orderId", null)
            return if (paymentJson != null) {
                val type = object : TypeToken<List<Payment>>() {}.type
                val paymentList: List<Payment> = gson.fromJson(paymentJson, type)
                paymentList.firstOrNull()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("PaymentRepository", "Error retrieving payment: ${e.message}")
            e.printStackTrace()
            return null
        }
    }

    /**
     * Retrieves all payments.
     * The payments are deserialized from JSON stored in SharedPreferences.
     *
     * @return A list of all retrieved payments, or an empty list if not found.
     *
     * @throws Exception if an error occurs while retrieving the payments.
     */
    fun getPayments(): ArrayList<Payment> {
        try {
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
        } catch (e: Exception) {
            Log.e("PaymentRepository", "Error retrieving all payments: ${e.message}")
            e.printStackTrace()
            return ArrayList()
        }
    }
}