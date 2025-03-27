package com.example.fooddream.controllers

import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.interfaces.ICustomerController

class CustomerController(view: AppCompatActivity): ICustomerController {

    private var accountController = AccountController(view)
    private var basketController = BasketController()
    private var sessionController = SessionController(view)
    private var navigationController = NavigationController(view)

    override fun handleRegistration(
        registerButton: Button,
        emailField: EditText,
        nameField: EditText,
        passwordField: EditText,
    ) {
        accountController.startRegistration(
            registerButton,
            emailField,
            nameField,
            passwordField,
        )
    }

    override fun handleLogin(
        loginButton: Button,
        emailField: EditText,
        passwordField: EditText,
    ) {
        accountController.startLogin(
            loginButton,
            emailField,
            passwordField,
        )
    }

    override fun startSession() {
        sessionController.startUserSession()
    }

    override fun clearSession() {
        sessionController.clearUserSession()
    }
}