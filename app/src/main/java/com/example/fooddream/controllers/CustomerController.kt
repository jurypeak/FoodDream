package com.example.fooddream.controllers

import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.interfaces.IAccountController
import com.example.fooddream.interfaces.IBasketController
import com.example.fooddream.interfaces.ICustomerController
import com.example.fooddream.messengers.Errors
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Customer
import com.example.fooddream.models.Product
import com.example.fooddream.views.VerifyEmailView
import org.json.JSONObject
import org.mindrot.jbcrypt.BCrypt

class CustomerController ( private var view: AppCompatActivity):
    IAccountController,
    IBasketController,
    ICustomerController {

    private val customer = Customer(
        fName = "",
        lName = "",
        email = "",
        accountId = -1,
        accessLevel = -1,
        password = ""
    )
    private var notification = Notification()

    override fun register(
        email: String,
        fName: String,
        lName: String,
        password: String
    ): Customer? {
        return try {
            null
        } catch (error: Errors.CreationException) {
            Log.d("Account Creation Error", "$error")
            null
        }
    }

    override fun resetPassword(newPassword: String, emailCode: Int): Boolean {
        //TODO add a helper function to verify email codes.
        return try {
            setHashedPassword(newPassword)
            true
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
            false
        }
    }

    override fun viewAccountDetails(): String {
        return ""
    }

    override fun deleteAccount(): Boolean {
        return try {
            true
        } catch (error: Errors.DeletionException) {
            Log.d("Deletion Error", "$error")
            false
        }
    }

    override fun editEmail(newEmail: String) {
        try {
            customer.setEmail(newEmail)
        } catch (error: Errors.SetException) {
            Log.d("Set Error", "$error")
        }
    }

    override fun editName(newFName: String, newLName: String) {
        try {
            customer.setFName(newFName)
            customer.setLName(newLName)
        } catch (error: Errors.SetException) {
            Log.d("Set Error", "$error")
        }
    }

    override fun editPassword(newPassword: String) {
        try {
            setHashedPassword(newPassword)
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
        }
    }

    override fun verifyEmail(
        email: String,
        requestQueue: RequestQueue,
        url: String
    ) {
        try {
            val jsonObject = JSONObject().apply {
                put("email", email)
            }
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    when {
                        response.getString("status") == "Success" -> {
                           val verificationCode = response.getString("verification_code")
                            Log.d("Code2", verificationCode)
                            Log.d("Response", "$response")
                            notification.sendNotification(
                                "Verification Code Sent.", view
                            )

                            val submitButton = view.findViewById<Button>(R.id.submit_button)
                            val codeField = view.findViewById<EditText>(R.id.code_verify)
                            handleVerificationCode(submitButton, codeField, verificationCode)
                        } else -> {
                            notification.sendNotification("Verification Failed To Send.", view)
                        }
                    }
                },
                { error ->
                    notification.sendNotification("$error.toString", view)
                    Log.d("Error", "$error")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: Errors.VerificationException) {
            Log.d("Verification Error", "$error")
        }
    }

    override fun addToBasket(product: Product) {
        try {

        } catch (error: Errors.BasketAdditionException) {
            Log.d("Basket Error", "$error")
        }
    }

    override fun viewBasket(): List<Product>? {
        return try {
            listOf<Product>()
        } catch (error: Errors.ViewBasketException) {
            Log.d("Basket Error", "$error")
            null
        }
    }

    override fun removeItem(id: Int) {
        TODO("Not yet implemented")
    }

    override fun editQuantity(id: Int, quantity: Int) {
        TODO("Not yet implemented")
    }

    override fun validateStock(): Boolean {
        TODO("Not yet implemented")
    }

    override fun toCheckout(): Boolean {
        TODO("Not yet implemented")
    }

    override fun viewOrder() {
        // TODO: Implement order retrieval logic
        Log.d("Order", "Fetching order details...")
    }

    override fun viewOrderHistory() {
        // TODO: Implement order history retrieval logic
        Log.d("Order History", "Fetching past orders...")
    }

    // Function to hash passwords without showing the algorithm
    override fun setHashedPassword(password: String): Boolean {
        return try {
            hashPassword(password).toString()
            true
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
            false
        }
    }

    // Function for encrypting password with BCrypt algorithm
    private fun hashPassword(password: String): String? {
        try {
            val salt = BCrypt.gensalt(12)
            val hashedPassword = BCrypt.hashpw(password, salt)
            return hashedPassword
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
        }
        return null
    }

    // Function to check inputted password against stored hashed password.
    private fun verifyPassword(
        password: String,
        hashedPassword: String
    ): Boolean {
        return try {
            BCrypt.checkpw(password, hashedPassword)
        } catch (error: Errors.ComparingException) {
            Log.d("Password Comparing Error", "$error")
            false
        }
    }

    // Function to allow users to login into their account.
    override fun login(
        email: String,
        password: String,
        requestQueue: RequestQueue,
        url: String
    ) {
        try {
            val jsonObject = JSONObject().apply {
                put("email", email)
                Log.d("Email", email)
                var password = hashPassword(password)
                put("password", password)
                Log.d("password", password.toString())
            }
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    Log.d("APIResponse", response.toString())
                    val returnedPassword = response.optString("password", "")
                    Log.d("passwordReturned", returnedPassword)
                    if (verifyPassword(password, returnedPassword)) {
                        Log.d("Response", "$response")
                        notification.sendNotification(
                            "Welcome ${
                                response.optString(
                                    "CustomerFName",
                                    ""
                                )
                            }", view
                        )
                        verifyEmail(email, requestQueue, BuildConfig.URL_VERIFY_EMAIL)
                        replaceWithFragment(VerifyEmailView())
                    } else {
                        notification.sendNotification("Password do not match", view)
                    }
                },
                { error ->
                    notification.sendNotification("$error.toString", view)
                    Log.d("Error", "$error")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: Errors.LoginException) {
            Log.d("Login Error", "$error")
        }
    }

    private fun replaceWithFragment(fragment: Fragment) {
        Log.d("FragmentTransaction", "Replacing fragment with ${fragment.javaClass.simpleName}")
        try {
            val transaction = view.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.verify_email_fragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
            Log.d("FragmentTransaction", "Fragment replaced successfully")
        } catch (e: Exception) {
            Log.e("FragmentTransaction", "Error replacing fragment: ${e.message}")
        }
    }



    // Function that closes users sessions and logs users out.
    override fun logout(
        sessionId: Int
    ): Boolean {
        //TODO Logout needs sessions to be implemented.
        return false
    }

    fun handleVerificationCode(
        submitButton: Button,
        codeField: EditText,
        verificationCode: String
    ) {
        submitButton.setOnClickListener {
            val inputtedCode = codeField.text.toString()

            Log.d("Code", verificationCode)
            Log.d("View", "$view")

            when {
                inputtedCode.isBlank() -> {
                    notification.sendNotification("Code cannot be empty.", view)
                }

                inputtedCode.length != 6 -> {
                    notification.sendNotification("Code must be 6 digits long.", view)
                }

                inputtedCode == verificationCode -> {
                    notification.sendNotification("Code is correct, account verified.", view)
                }

                else -> {
                    notification.sendNotification("Incorrect verification code.", view)
                }
            }
        }
    }

    override fun handleLogin(
        loginButton: Button,
        emailField: EditText,
        passwordField: EditText,
        url: String
    ) {
        val requestQueue = Volley.newRequestQueue(view)

        loginButton.setOnClickListener {
            var email = emailField.text.toString()
            var password = passwordField.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                Log.d("Login", "$email, $password")
                login(email, password, requestQueue, url)
            } else {
                notification.sendNotification("Email or password cannot be empty.", view)
                Log.e("MainActivity", "Email or password cannot be empty.")
            }
        }
    }
}