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

/**
 * AccountManager is responsible for managing user accounts in the application.
 * It provides methods to update and delete user accounts, as well as encrypt passwords.
 * This class interacts with the server to perform account-related operations.
 * It uses Volley for network requests and handles JSON responses.
 *
 * @param view The activity context used for account management.
 */
class AccountManager(view: AppCompatActivity) {

    // Repositories and controllers for handling data and operations
    private val basketRepository = BasketRepository(view)
    private val sessionController = SessionController(view)
    private val navigationController = NavigationController(view)
    private val customerRepository = CustomerRepository(view)
    private var notification = com.example.fooddream.messengers.Notification()

    /**
     * Updates the user account with the provided details.
     * This method sends a JSON object to the server with the updated account information.
     *
     * @param email The email address of the user.
     * @param fName The first name of the user.
     * @param lName The last name of the user.
     * @param password The new password for the user account.
     * @param view The activity context used for displaying notifications and managing UI interactions.
     * @param requestQueue The Volley request queue for network operations.
     * @param url The URL endpoint for updating the account on the server.
     *
     * @throws Errors.LoginException if an error occurs while updating the account.
     */
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

    /**
     * Deletes the user account from the server.
     * This method sends a JSON object to the server with the account ID to be deleted.
     *
     * @param view The activity context used for displaying notifications and managing UI interactions.
     * @param requestQueue The Volley request queue for network operations.
     * @param url The URL endpoint for deleting the account on the server.
     *
     * @throws Errors.LoginException if an error occurs while deleting the account.
     */
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

    /**
     * Encrypts the provided password using BCrypt hashing algorithm.
     * This method generates a salt and hashes the password for secure storage.
     *
     * @param password The password to be encrypted.
     * @return The hashed password as a string, or null if an error occurs.
     *
     * @throws Errors.HashingException if an error occurs while hashing the password.
     */
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