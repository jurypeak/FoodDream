package com.example.fooddream.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.fooddream.models.Address
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * AddressRepository is responsible for managing address data in the application.
 * It provides methods to save and retrieve addresses using SharedPreferences.
 *
 * @param view The activity context used for SharedPreferences operations.
 */
class AddressRepository (private var view: AppCompatActivity){
    /**
     * SharedPreferences instance to store address data.
     * This instance is used to save and retrieve addresses using JSON serialization.
     */
    private val sharedPreferences: SharedPreferences = view.getSharedPreferences("address_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * Saves a list of addresses for a specific order ID.
     * The addresses are serialized to JSON and stored in SharedPreferences.
     *
     * @param orderId The ID of the order associated with the addresses.
     * @param addresses The list of addresses to be saved.
     *
     * @throws Exception if an error occurs while saving the addresses.
     */
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

    /**
     * Retrieves a single address for a specific order ID.
     * The address is deserialized from JSON stored in SharedPreferences.
     *
     * @param orderId The ID of the order associated with the address.
     * @return The retrieved address, or null if not found.
     *
     * @throws Exception if an error occurs while retrieving the address.
     */
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

    /**
     * Retrieves a list of addresses for a specific order ID.
     * The addresses are deserialized from JSON stored in SharedPreferences.
     *
     * @param orderId The ID of the order associated with the addresses.
     * @return The list of retrieved addresses, or an empty list if not found.
     *
     * @throws Exception if an error occurs while retrieving the addresses.
     */
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