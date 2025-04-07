package com.example.fooddream.utils

import CustomerRepository
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.messengers.Errors
import com.example.fooddream.repositories.BasketRepository
import com.example.fooddream.views.CustomerCatalogView
import org.json.JSONObject

class OrderManager(
    private val view: AppCompatActivity,
    private val customerRepository: CustomerRepository,
) {
    private val basketRepository = BasketRepository(view)
    private val navigationController = NavigationController(view)
    private var notification = com.example.fooddream.messengers.Notification()
    private var orderId = 0

    fun handleOrder(
        email: String,
        fName: String,
        lName: String,
        address: String,
        town: String,
        postcode: String,
        paymentMethod: String,
        requestQueue: RequestQueue,
        urlOrder: String,
        urlPayment: String,
        urlAddress: String,
        urlOrderItems: String
    ) {
        try {
            val jsonObject = JSONObject().apply {
                put("email", email)
                put("fName", fName)
                put("lName", lName)
                put("accountId", customerRepository.getCustomer()?.getAccountId())
            }
            Log.d("Order", "$email, $fName, $lName, ${customerRepository.getCustomer()?.getAccountId()}")
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, urlOrder, jsonObject,
                { response ->
                    val returnedResponseStatus = response.optString("status", "")
                    if (returnedResponseStatus == "Success") {
                        orderId = response.optInt("orderId")
                        handleOrderItems(
                            requestQueue,
                            urlOrderItems
                        )
                        handleAddress(
                            address,
                            town,
                            postcode,
                            requestQueue,
                            urlAddress
                        )
                        handlePayment(
                            paymentMethod,
                            requestQueue,
                            urlPayment
                        )
                        notification.sendNotification("Order placed successfully", view)
                        Log.d("Response", "$response")
                        navigationController.navigateToActivity(CustomerCatalogView::class.java)
                    } else {
                        notification.sendNotification("${response.optString("message", "")}", view)
                        Log.d("Response", "$response")
                    }
                },
                { error ->
                    notification.sendNotification(error.toString(), view)
                    Log.d("Volley Error", "$error")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: Errors.LoginException) {
            Log.d("Order Error", "$error")
        }
    }

    fun handleOrderItems(
        requestQueue: RequestQueue,
        url: String,
    ) {
        for (basketItem in basketRepository.getAllBasketItems()) {
            try {
                val jsonObject = JSONObject().apply {
                    put("orderId", orderId)
                    put("productId", basketItem.getProductId())
                    put("quantity", basketItem.getQuantity())
                    put("price", basketItem.getPrice())
                    put("productName", basketItem.getItemName())
                }
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    { response ->
                        val returnedResponseStatus = response.optString("status", "")
                        if (returnedResponseStatus == "Success") {
                            Log.d("Response", "$response")
                        } else {
                            notification.sendNotification("${response.optString("message", "")}", view)
                            Log.d("Response", "$response")
                        }
                    },
                    { error ->
                        notification.sendNotification(error.toString(), view)
                        Log.d("Volley Error", "$error")
                    })
                requestQueue.add(jsonObjectRequest)
            } catch (error: Errors.LoginException) {
                Log.d("Order Error", "$error")
            }
        }
    }

    fun handleAddress(
        address: String,
        town: String,
        postcode: String,
        requestQueue: RequestQueue,
        url: String
    ) {
        try {
            val jsonObject = JSONObject().apply {
                put("address", address)
                put("town", town)
                put("postcode", postcode)
                put("orderId", orderId)
            }
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    val returnedResponseStatus = response.optString("status", "")
                    if (returnedResponseStatus == "Success") {
                        Log.d("Response", "$response")
                    } else {
                        notification.sendNotification("${response.optString("message", "")}", view)
                        Log.d("Response", "$response")
                    }
                },
                { error ->
                    notification.sendNotification(error.toString(), view)
                    Log.d("Volley Error", "$error")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: Errors.LoginException) {
            Log.d("Order Error", "$error")
        }
    }

    fun handlePayment(
        paymentMethod: String,
        requestQueue: RequestQueue,
        url: String
    ) {
        try {
            val jsonObject = JSONObject().apply {
                put("paymentMethod", paymentMethod)
                put("amount", basketRepository.getBasketTotalPrice())
                put("orderId", orderId)
            }
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    val returnedResponseStatus = response.optString("status", "")
                    if (returnedResponseStatus == "Success") {
                        Log.d("Response", "$response")
                    } else {
                        notification.sendNotification("${response.optString("message", "")}", view)
                        Log.d("Response", "$response")
                    }
                },
                { error ->
                    notification.sendNotification(error.toString(), view)
                    Log.d("Volley Error", "$error")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: Errors.LoginException) {
            Log.d("Order Error", "$error")
        }
    }
}