package com.example.fooddream.controllers

import CustomerRepository
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.interfaces.IAccountController
import com.example.fooddream.messengers.Errors
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Customer
import com.example.fooddream.utils.AccountManager
import com.example.fooddream.utils.AuthenticationManager
import com.example.fooddream.views.LoginView
import com.example.fooddream.views.VerifyEmailView

/**
 * AccountController is responsible for handling user account-related actions such as login, registration and password reset.
 *
 * @param view The activity context in which the controller operates.
 */
class AccountController(private var view: AppCompatActivity): IAccountController {

    // Model for the customer account.
    private val customer = Customer(
        fName = "",
        lName = "",
        email = "",
        accountId = -1,
        accessLevel = -1,
        password = ""
    )

    // Repositories, controllers and managers for handling user data and actions.
    private var customerRepository = CustomerRepository(view)
    private var notification = Notification()
    private var basketRepository = com.example.fooddream.repositories.BasketRepository(view)
    private var navigationController = NavigationController(view)
    private var sessionController = SessionController(view)
    private var validateManager = com.example.fooddream.utils.ValidateManager()
    private var accountManager = AccountManager(view)
    private var authenticationManager = AuthenticationManager(
        view,
        customer,
        navigationController,
        sessionController
    )

    /**
     * Sends a two-factor authentication code to the user's email.
     *
     * @param email The email address to send the verification code to.
     * @param requestQueue The request queue for network operations.
     * @param url The URL for sending the verification code.
     * @param typeView The type of view (e.g., "Login", "Register").
     */
    override fun sendTwoFactorAuth(
        email: String,
        requestQueue: RequestQueue,
        url: String,
        typeView: String
    ) {
        authenticationManager.sendVerificationEmailCode(
            email,
            requestQueue,
            url,
            typeView
        )
    }

    /**
     * Starts the login process by validating the input fields and calling the authentication manager.
     *
     * @param emailField The EditText for entering the email address.
     * @param passwordField The EditText for entering the password.
     *
     * @throws Exception if an error occurs during the process.
     */
    override fun startLogin(
        emailField: EditText,
        passwordField: EditText,
    ) {
        try {
            val requestQueue = Volley.newRequestQueue(view)
            var email = emailField.text.toString()
            var password = passwordField.text.toString()

            customer.setEmail(email)
            customer.setPassword(password)

            if (email.isBlank() && password.isBlank()) {
                notification.sendNotification("Email or password cannot be empty.", view)
                Log.e("AccountController", "Email or password cannot be empty.")
            }
            if (!validateManager.isValidEmail(emailField.text.toString())) {
                notification.sendNotification(
                    "Invalid email format.",
                    emailField.context as AppCompatActivity
                )
            }
            if (!validateManager.isValidPassword(passwordField.text.toString())) {
                notification.sendNotification("Invalid password format.", passwordField.context as AppCompatActivity)
            }
            else {
                Log.d("Login", "$email, $password")
                authenticationManager.login(
                    email,
                    password,
                    requestQueue,
                    BuildConfig.URL_LOGIN
                )
            }
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while logging in", view)
            Log.d("Login Handling Error", "$error")
        }
    }

    /**
     * Starts the registration process by validating the input fields and calling the authentication manager.
     *
     * @param emailField The EditText for entering the email address.
     * @param nameField The EditText for entering the name.
     * @param passwordField The EditText for entering the password.
     *
     * @throws Exception if an error occurs during the process.
     */
    override fun startRegistration(
        emailField: EditText,
        nameField: EditText,
        passwordField: EditText,
    ) {
        try {
            val email = emailField.text.toString()
            val name = nameField.text.toString()
            val password = passwordField.text.toString()

            if (email.isBlank() || name.isBlank() || password.isBlank()) {
                notification.sendNotification("All fields need to be entered.", view)
                Log.e("AccountController", "Email, name or password cannot be empty.")
            }
            if (!validateManager.isValidEmail(emailField.text.toString())) {
                notification.sendNotification("Invalid email format.", emailField.context as AppCompatActivity)
                Log.e("AccountController", "Invalid email format.")
            }
            if (!validateManager.isValidName(nameField.text.toString())) {
                notification.sendNotification("Invalid name format.", nameField.context as AppCompatActivity)
                Log.e("AccountController", "Invalid name format.")
            }
            if (!validateManager.isValidPassword(passwordField.text.toString())) {
                notification.sendNotification("Invalid password format.", passwordField.context as AppCompatActivity)
                Log.e("AccountController", "Invalid password format.")
            }
            else {
                val nameParts = name.split(" ")
                val fName = nameParts.getOrNull(0) ?: ""
                val lName = nameParts.getOrNull(1) ?: ""

                customer.setFName(fName)
                customer.setFName(lName)
                customer.setEmail(email)
                customer.setPassword(password)
                customer.setAccessLevel(1)

                authenticationManager.register(
                    email,
                    fName,
                    lName,
                    password,
                    Volley.newRequestQueue(view),
                    BuildConfig.URL_REGISTER
                )
            }
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while registering", view)
            Log.d("Register Handling Error", "$error")
        }
    }

    /**
     * Starts the password reset process by validating the email inputted and calling the authentication manager.
     *
     * @param emailField The EditText for entering the email address.
     *
     * @throws Exception if an error occurs during the process.
     */
    override fun startResetPasswordProcess(
        emailField: EditText,
    ) {
        try {
            val email = emailField.text.toString()
            customer.setEmail(email)
            customerRepository.saveCustomer(customer)

            if (email.isBlank()) {
                notification.sendNotification("Email cannot be empty.", emailField.context as AppCompatActivity)
                Log.e("AccountController", "Email cannot be empty.")
            }
            if (!validateManager.isValidEmail(emailField.text.toString())) {
                notification.sendNotification("Invalid email format.", emailField.context as AppCompatActivity)
            }
            else {
                customer.setEmail(email)
                customerRepository.saveCustomer(customer)
                val bundle = Bundle().apply {
                    putString("email", email)
                    putString("typeView", "Reset Password")
                }
                val verifyEmailFragment = VerifyEmailView()
                verifyEmailFragment.arguments = bundle
                navigationController.replaceActivityWithFragment(
                    verifyEmailFragment,
                    R.id.fragment_container
                )
            }
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while registering", view)
            Log.d("Register Handling Error", "$error")
        }
    }

    /**
     * Validates the new password entered by the user and calls the authentication manager to reset the password.
     *
     * @param passwordField The EditText for entering the new password.
     *
     * @throws Exception if an error occurs during the process.
     */
    override fun validateNewResetPassword(
        passwordField: EditText,
    ) {
        try {
            val password = passwordField.text.toString()
            if (password.isBlank()) {
                notification.sendNotification("Password cannot be empty.", passwordField.context as AppCompatActivity)
                Log.e("AccountController", "Password cannot be empty.")
            }
            if (!validateManager.isValidPassword(passwordField.text.toString())) {
                notification.sendNotification("Invalid password format.", passwordField.context as AppCompatActivity)
                Log.e("AccountController", "Invalid password format.")
            }
            else {
                try {
                    authenticationManager.resetPassword(
                        customerRepository.getCustomer()?.getEmail().toString(),
                        password,
                        Volley.newRequestQueue(view),
                        BuildConfig.URL_RESETPASSWORD
                    )
                } catch (error: Exception) {
                    notification.sendNotification("Error occurred while registering", view)
                    Log.d("Register Handling Error", "$error")
                }
            }
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while registering", view)
            Log.d("Register Handling Error", "$error")
        }
    }

    /**
     * Deletes the user account by calling the account manager.
     *
     * @param view The activity context used to initialize the controllers.
     * @param requestQueue The request queue for network operations.
     * @param url The URL for deleting the account.
     *
     * @throws Exception if an error occurs during the process.
     */
    override fun deleteAccount(
        view: AppCompatActivity,
        requestQueue: RequestQueue,
        url: String
    ) {
        try {
            accountManager.deleteAccount(
                view,
                requestQueue,
                url
            )
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while deleting account", view)
            Log.d("AccountController Error", "$error")
        }
    }

    /**
     * Edits the account details of the user by calling the account manager.
     *
     * @param view The activity context used to initialize the controllers.
     * @param fName The first name of the user.
     * @param lName The last name of the user.
     * @param email The email address of the user.
     * @param password The new password for the user.
     *
     * @throws Exception if an error occurs during the process.
     */
    override fun editAccountDetails(
        view: AppCompatActivity,
        fName: String,
        lName: String,
        email: String,
        password: String,
    ) {
        try {
            if (!validateManager.isValidEmail(email)) {
                notification.sendNotification("Invalid email format.", view)
                return
            }
            if (!validateManager.isValidPassword(password)) {
                notification.sendNotification("Invalid password format. Password size must be larger than or equal to 6", view)
                return
            }
            if (!validateManager.isValidName(fName)) {
                notification.sendNotification("Invalid first name format.", view)
                return
            }
            if (!validateManager.isValidName(lName)) {
                notification.sendNotification("Invalid last name format.", view)
                return
            }
            else {
                accountManager.updateAccount(
                    email,
                    fName,
                    lName,
                    password,
                    view,
                    Volley.newRequestQueue(view),
                    BuildConfig.URL_UPDATE_ACCOUNT
                )
            }
        } catch (error: Errors.SetException) {
            Log.d("AccountController Error", "$error")
        }
    }

    /**
     * Logs out the user by clearing the session and navigating to the login view.
     *
     * @throws Exception if an error occurs during the logout process.
     */
    override fun logout(
    ) {
        try {
            basketRepository.clearBasket()
            sessionController.clearUserSession()
            customerRepository.deleteCustomer()
            navigationController.navigateToActivity(LoginView::class.java)
            notification.sendNotification("Logged out successfully", view)
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while logging out", view)
            Log.d("Logout Error", "$error")
        }
    }
}