package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.fooddream.models.Address
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddressRepository (private var view: AppCompatActivity){
    private val sharedPreferences: SharedPreferences =
        view.getSharedPreferences("address_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveAddresses(orderId: Int, addresses: List<Address>) {
        try {
            val addressJson = gson.toJson(addresses)
            sharedPreferences.edit() {
                putString("address_$orderId", addressJson)
            }
        } catch (e: Exception) {
            Log.e("AddressRepository", "Error saving addresses: ${e.message}")
            e.printStackTrace()
        }
    }

    fun getAddress(orderId: Int): Address? {
        try {
            val addressJson = sharedPreferences.getString("address_$orderId", null)
            return if (!addressJson.isNullOrEmpty()) {
                val type = object : TypeToken<List<Address>>() {}.type
                val addressList: List<Address> = gson.fromJson(addressJson, type)
                addressList.firstOrNull()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("AddressRepository", "Error retrieving address: ${e.message}")
            e.printStackTrace()
            return null
        }
    }

    fun getAddresses(orderId: Int): List<Address> {
        try {
            val addressJson = sharedPreferences.getString("address_$orderId", null)
            return if (!addressJson.isNullOrEmpty()) {
                val type = object : TypeToken<List<Address>>() {}.type
                gson.fromJson(addressJson, type) ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("AddressRepository", "Error retrieving addresses: ${e.message}")
            e.printStackTrace()
            return emptyList()
        }
    }
}