package com.example.fooddream.utils

import CustomerRepository
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.fooddream.R
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.messengers.Errors
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Customer
import com.example.fooddream.views.AdminCatalogView
import com.example.fooddream.views.CustomerCatalogView
import com.example.fooddream.views.LoginView
import com.example.fooddream.views.ResetPasswordView
import com.example.fooddream.views.VerifyEmailView
import org.json.JSONObject
import org.mindrot.jbcrypt.BCrypt

/**
 * AuthenticationManager is responsible for handling user authentication, including registration,
 * login, password reset, and email verification.
 *
 * @property view The activity where the authentication is taking place.
 * @property customer The customer object representing the user.
 * @property navigationController The controller for managing navigation between activities/fragments.
 * @property sessionController The controller for managing user sessions.
 */
class AuthenticationManager(
    private var view: AppCompatActivity,
    private val customer: Customer,
    private var navigationController: NavigationController,
    private var sessionController: SessionController
) {

    private var notification = Notification()
    private var customerRepository = CustomerRepository(view)

    /**
     * Function to register a new user account.
     *
     * @param email The email address of the user.
     * @param fName The first name of the user.
     * @param lName The last name of the user.
     * @param password The password for the account.
     * @param requestQueue The request queue for making network requests.
     * @param url The URL for the registration endpoint.
     *
     * @throws Exception if an error occurs during the registration process.
     */
    fun register(
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
                        customerRepository.saveCustomer(customer)
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
        } catch (error: Exception) {
            Log.d("Registration Error", "$error")
        }
    }

    /**
     * Function to log in an existing user account.
     *
     * @param email The email address of the user.
     * @param password The password for the account.
     * @param requestQueue The request queue for making network requests.
     * @param url The URL for the login endpoint.
     *
     * @throws Exception if an error occurs during the login process.
     */
    fun login(
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
                    else {
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
                                putString("typeView", "Login")
                            }

                            if (response.optInt("accessLevel", -1) == 0) {
                                navigationController.navigateToActivity(AdminCatalogView::class.java)
                            }

                            if (response.optInt("accessLevel", -1) == 1) {
                                customer.setAccountId(response.optInt("id"))
                                customer.setEmail(response.optString("email", ""))
                                customer.setFName(response.optString("CustomerFName", ""))
                                customer.setLName(response.optString("CustomerLName", ""))
                                customerRepository.saveCustomer(customer)
                                val verifyEmailFragment = VerifyEmailView()
                                verifyEmailFragment.arguments = bundle
                                view.supportFragmentManager.popBackStack()
                                navigationController.replaceActivityWithFragment(
                                    verifyEmailFragment,
                                    R.id.fragment_container
                                )
                            }

                            else {
                                notification.sendNotification("Account not verified", view)
                                Log.d("Response", "$response")
                            }

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

    /**
     * Function to reset the password for an existing user account.
     *
     * @param email The email address of the user.
     * @param password The new password for the account.
     * @param requestQueue The request queue for making network requests.
     * @param url The URL for the password reset endpoint.
     *
     * @throws Exception if an error occurs during the password reset process.
     */
    fun resetPassword(
        email: String,
        password: String,
        requestQueue: RequestQueue,
        url: String
    ) {
        try {
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
                        navigationController.navigateToActivity(LoginView::class.java)
                    }
                    else {
                        notification.sendNotification("${response.optString("message", "")}", view)
                        Log.d("Response", "$response")
                        navigationController.navigateToActivity(LoginView::class.java)
                    }
                },
                { error ->
                    notification.sendNotification("$error.toString", view)
                    Log.d("Authentication Error", "$error")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: Errors.LoginException) {
            Log.d("Authentication Error", "$error")
        }
    }

    /**
     * Function to verify the email code entered by the user.
     *
     * @param submitButton The button to submit the verification code.
     * @param codeField The field for entering the verification code.
     * @param verificationCode The actual verification code sent to the user's email.
     * @param typeView The type of view to be displayed after verification.
     *
     * @throws Exception if an error occurs during the verification process.
     */
    fun verifyUserEmailCode(
        submitButton: Button,
        codeField: EditText,
        verificationCode: String,
        typeView: String
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
                        if (typeView == "Login") {
                            sessionController.startUserSession()
                            navigationController.navigateToActivity(CustomerCatalogView::class.java)
                        }
                        if (typeView == "Reset Password") {
                            navigationController.navigateToFragment(
                                ResetPasswordView(),
                                R.id.fragment_container
                            )
                        }
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

    /**
     * Function to send a verification email code to the user.
     *
     * @param email The email address of the user.
     * @param requestQueue The request queue for making network requests.
     * @param url The URL for the verification endpoint.
     * @param typeView The type of view to be displayed after sending the verification code.
     *
     * @throws Exception if an error occurs during the email verification process.
     */
    fun sendVerificationEmailCode(
        email: String,
        requestQueue: RequestQueue,
        url: String,
        typeView: String
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
                                verificationCode,
                                typeView
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

    /**
     * Function to verify the password entered by the user.
     *
     * @param password The password entered by the user.
     * @param hashedPassword The hashed password stored in the database.
     *
     * @throws Exception if an error occurs during the password verification process.
     */
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

    /**
     * Function to encrypt the password using BCrypt hashing algorithm.
     *
     * @param password The password to be encrypted.
     *
     * @throws Exception if an error occurs during the password encryption process.
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