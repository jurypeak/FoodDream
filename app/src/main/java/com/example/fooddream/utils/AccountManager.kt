package com.example.fooddream.utils

import CustomerRepository
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.messengers.Errors
import com.example.fooddream.repositories.BasketRepository
import com.example.fooddream.views.CustomerCatalogView
import com.example.fooddream.views.LoginView
import org.json.JSONObject
import org.mindrot.jbcrypt.BCrypt

class AccountManager(view: AppCompatActivity) {

    private val basketRepository = BasketRepository(view)
    private val sessionController = SessionController(view)
    private val navigationController = NavigationController(view)
    private val customerRepository = CustomerRepository(view)
    private var notification = com.example.fooddream.messengers.Notification()

    fun updateAccount(
        email: String,
        fName: String,
        lName: String,
        password: String,
        view: AppCompatActivity,
        requestQueue: RequestQueue,
        url: String
    ) {
        try {
            val jsonObject = JSONObject().apply {
                var password = encryptPassword(password)
                put("email", email)
                put("fName", fName)
                put("lName", lName)
                put("password", password)
                put("accountId", customerRepository.getCustomer()?.getAccountId())
            }
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    if (response.optString("status","") != "Success") {
                        notification.sendNotification("${response.optString("message", "")}", view)
                        Log.d("Response", "$response")
                    }
                    else {
                        notification.sendNotification("${response.optString("message", "")}", view)
                        Log.d("Response", "$response")
                        sessionController.clearUserSession()
                        navigationController.navigateToActivity(CustomerCatalogView::class.java)
                    }
                },
                { error ->
                    notification.sendNotification("$error.toString", view)
                    Log.d("Account Error", "$error")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: Errors.LoginException) {
            Log.d("Account Error", "$error")
        }
    }

    fun deleteAccount(
        view: AppCompatActivity,
        requestQueue: RequestQueue,
        url: String
    ) {
        try {
            val jsonObject = JSONObject().apply {
                put("accountId", customerRepository.getCustomer()?.getAccountId())
            }
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    if (response.optString("status","") != "Success") {
                        notification.sendNotification("${response.optString("message", "")}", view)
                        Log.d("Response", "$response")
                    }
                    else {
                        notification.sendNotification("${response.optString("message", "")}", view)
                        Log.d("Response", "$response")
                        basketRepository.clearBasket()
                        sessionController.clearUserSession()
                        customerRepository.deleteCustomer()
                        navigationController.navigateToActivity(LoginView::class.java)
                    }
                },
                { error ->
                    notification.sendNotification("$error.toString", view)
                    Log.d("Account Error", "$error")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: Errors.LoginException) {
            Log.d("Account Error", "$error")
        }
    }

    private fun encryptPassword(password: String): String? {
        try {
            val salt = BCrypt.gensalt(12)
            val hashedPassword = BCrypt.hashpw(password, salt)
            return hashedPassword
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
        }
        return null
    }
}