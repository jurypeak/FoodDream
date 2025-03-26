package com.example.fooddream.controllers

import android.os.Bundle
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
import com.example.fooddream.views.LoginView
import com.example.fooddream.views.RegisterView
import com.example.fooddream.views.VerifyEmailView
import org.json.JSONObject
import org.mindrot.jbcrypt.BCrypt

class CustomerController (
    private var view: AppCompatActivity
):
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
        password: String,
        requestQueue: RequestQueue,
        url: String
    ) {
        try {
            val jsonObject = JSONObject().apply {
                var encryptedPassword = encryptPassword(password)
                put("email", email)
                put("fName", fName)
                put("lName", lName)
                put("password", encryptedPassword)
                put("accessLevel", 1)
            }
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    val returnedResponseStatus = response.optString("status", "")
                    if (returnedResponseStatus == "Success") {
                        notification.sendNotification(
                            "Welcome $fName", view
                        )
                        customer.setAccountId(response.optString("accountId", "").toInt())
                        view.supportFragmentManager.popBackStack()
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
            Log.d("Registration Error", "$error")
        }
    }

    override fun resetPassword(newPassword: String, emailCode: Int): Boolean {
        //TODO add a helper function to verify email codes.
        return try {
            setEncryptedPassword(newPassword)
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
            setEncryptedPassword(newPassword)
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
        }
    }

    override fun verifyUserEmailCode(
        submitButton: Button,
        codeField: EditText,
        verificationCode: String
    ) {
        try {
            submitButton.setOnClickListener {
                val inputtedCode = codeField.text.toString()
                when {
                    inputtedCode.isBlank() -> {
                        notification.sendNotification("Code cannot be empty.", view)
                    }
                    inputtedCode.length != 6 -> {
                        notification.sendNotification("Code must be 6 digits long.", view)
                    }
                    inputtedCode == verificationCode -> {
                        notification.sendNotification("Code is correct, account verified.", view)
                        view.supportFragmentManager.popBackStack()
                    }
                    else -> {
                        notification.sendNotification("Incorrect verification code.", view)
                    }
                }
            }
        } catch (error: Exception) {
            notification.sendNotification("Error occured while verifying email.", view)
            Log.d("Email Verification Handling Error", "$error")
        }
    }

    override fun sendVerificationEmailCode(
        email: String,
        requestQueue: RequestQueue,
        url: String,
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
                            notification.sendNotification(
                                "Verification Code Sent.", view
                            )
                            Log.d("Response", "$response")
                            val submitButton = view.findViewById<Button>(R.id.submit_button)
                            val codeField = view.findViewById<EditText>(R.id.code_verify)
                            verifyUserEmailCode(
                                submitButton,
                                codeField,
                                verificationCode
                            )
                        } else -> {
                            notification.sendNotification("Verification Failed To Send.", view)
                            Log.d("Response", "$response")
                        }
                    }
                },
                { error ->
                    notification.sendNotification("$error.toString", view)
                    Log.d("Send Verify Error", "$error")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: Errors.VerificationException) {
            Log.d("Send Verify Error", "$error")
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
    override fun setEncryptedPassword(password: String): Boolean {
        return try {
            encryptPassword(password).toString()
            true
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
            false
        }
    }

    // Function for encrypting password with BCrypt algorithm
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
            customer.setEmail(email)
            val jsonObject = JSONObject().apply {
                var password = encryptPassword(password)
                put("email", email)
                put("password", password)
            }
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    if (response.optString("status","") != "Success") {
                        notification.sendNotification("${response.optString("message", "")}", view)
                        Log.d("Response", "$response")
                    }
                    else{
                        val returnedPassword = response.optString("password", "")
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
                            val bundle = Bundle().apply {
                                putString("email", email)
                            }
                            val verifyEmailFragment = VerifyEmailView()
                            verifyEmailFragment.arguments = bundle
                            replaceActivityWithFragment(verifyEmailFragment, R.id.verify_email_fragment)
                        } else {
                            notification.sendNotification("Password do not match", view)
                            Log.d("Response", "$response")
                        }
                    }
                },
                { error ->
                    notification.sendNotification("$error.toString", view)
                    Log.d("Login Error", "$error")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: Errors.LoginException) {
            Log.d("Login Error", "$error")
        }
    }

    // Function that closes users sessions and logs users out.
    override fun logout(
        sessionId: Int
    ): Boolean {
        //TODO Logout needs sessions to be implemented.
        return false
    }

    override fun startRegistration(
        registerButton: Button,
        emailField: EditText,
        nameField: EditText,
        passwordField: EditText,
    ) {
        try {
            registerButton.setOnClickListener {
                val email = emailField.text.toString()
                val name = nameField.text.toString()
                val password = passwordField.text.toString()

                if (email.isNotBlank() && name.isNotBlank() && password.isNotBlank()) {
                    val nameParts = name.split(" ")
                    val fName = nameParts.getOrNull(0) ?: ""
                    val lName = nameParts.getOrNull(1) ?: ""

                    customer.setFName(fName)
                    customer.setFName(lName)
                    customer.setEmail(email)
                    customer.setPassword(password)
                    customer.setAccessLevel(1)

                    register(
                        email,
                        fName,
                        lName,
                        password,
                        Volley.newRequestQueue(view),
                        BuildConfig.URL_REGISTER
                    )

                }
            }
        } catch (error: Exception) {
            notification.sendNotification("Error occured while registering", view)
            Log.d("Register Handling Error", "$error")
        }
    }

    override fun startLogin(
        loginButton: Button,
        emailField: EditText,
        passwordField: EditText,
        url: String
    ) {
        try {
            val requestQueue = Volley.newRequestQueue(view)
            var email = emailField.text.toString()
            var password = passwordField.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                Log.d("Login", "$email, $password")
                login(email, password, requestQueue, url)
            } else {
                notification.sendNotification("Email or password cannot be empty.", view)
                Log.e("MainActivity", "Email or password cannot be empty.")
            }
        } catch (error: Exception) {
            notification.sendNotification("Error occured while logging in", view)
            Log.d("Login Handling Error", "$error")
        }
    }

    fun replaceActivityWithFragment(fragment: Fragment, id: Int) {
        Log.d("FragmentTransaction", "Replacing fragment with ${fragment.javaClass.simpleName}")
        try {
            val transaction = view.supportFragmentManager.beginTransaction()
            transaction.replace(id, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
            Log.d("FragmentTransaction", "Fragment replaced successfully")
        } catch (e: Exception) {
            Log.e("FragmentTransaction", "Error replacing fragment: ${e.message}")
        }
    }

    fun createRegisterView() {
        replaceActivityWithFragment(RegisterView(), R.id.register_fragment)
    }
}