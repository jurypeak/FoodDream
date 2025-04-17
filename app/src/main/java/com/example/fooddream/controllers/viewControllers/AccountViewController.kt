package com.example.fooddream.controllers.viewControllers

import CustomerRepository
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.controllers.AccountController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Order
import com.example.fooddream.models.Payment
import com.example.fooddream.models.Product
import com.example.fooddream.repositories.OrderRepository
import com.example.fooddream.utils.OrderManager
import com.example.fooddream.views.OrderHistoryView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat

/**
 * AccountViewController is responsible for managing the account view in the application.
 * It handles user interactions, updates account details, and manages order history.
 *
 * @property customerRepository Repository for managing customer data.
 * @property orderRepository Repository for managing order data.
 * @property accountController Controller for managing account-related actions.
 * @property navigationController Controller for managing navigation actions.
 * @property notification Notification manager for displaying messages to the user.
 */
class AccountViewController(
    private val customerRepository: CustomerRepository,
    private val orderRepository: OrderRepository,
    private val accountController: AccountController,
    private val navigationController: NavigationController,
    private val notification: Notification,
) {

    private val validateManager = com.example.fooddream.utils.ValidateManager()

    /**
     * Checks if there are any orders associated with the user account.
     * If orders exist, it updates the order details in the UI.
     *
     * @param currencyFormat The format for displaying currency values.
     * @param orderNumberField TextView for displaying the order number.
     * @param orderDateField TextView for displaying the order date.
     * @param orderTotalField TextView for displaying the order total.
     * @param payments List of payments associated with the orders.
     * @param orders List of orders associated with the user account.
     *
     * @throws Exception if an error occurs while checking orders.
     */
    @SuppressLint("SetTextI18n")
    fun checkIfOrdersExist(
        currencyFormat: NumberFormat,
        orderNumberField: TextView,
        orderDateField: TextView,
        orderTotalField: TextView,
        payments: List<Payment>,
        orders: List<Order>
    ) {
        if (orders.isNotEmpty()) {
            orderNumberField.text = "Order #${orders.last().getOrderId()}"
            orderDateField.text = orders.last().getOrderDate()
            orderTotalField.text = currencyFormat.format(payments.last().getAmount())
        } else {
            orderNumberField.text = ""
            orderDateField.text = "No Orders"
            orderTotalField.text = ""
        }
    }

    /**
     * Initializes the account screen by loading the user's orders.
     *
     * @param context The context of the application.
     * @param view The activity where the account screen is displayed.
     * @param orderManager The manager for handling order-related actions.
     *
     * @throws Exception if an error occurs while loading orders.
     */
    fun initializeAccountScreen(
        context: Context,
        view: AppCompatActivity,
        orderManager: OrderManager,
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val accountId = customerRepository.getCustomer()?.getAccountId()
                if (accountId != null) {
                    orderManager.getOrders(
                        Volley.newRequestQueue(context),
                        BuildConfig.URL_GET_ORDERS,
                        accountId
                    )
                }
            } catch (e: Exception) {
                notification.sendNotification("Error occurred while loading orders on account page.", view)
                Log.d("AccountView", "Error: $e")
            }
        }
    }

    /**
     * Sets up click listeners for various UI components in the account view.
     *
     * @param view The activity where the account view is displayed.
     * @param context The context of the application.
     * @param nameField The field for entering the user's name.
     * @param emailField The field for entering the user's email.
     * @param passwordField The field for entering the user's password.
     * @param updateButton The button to update account details.
     * @param deleteAccountButton The button to delete the account.
     * @param logOutButton The button to log out of the account.
     * @param orderHistoryLayout The layout for displaying order history.
     *
     * @throws Exception if an error occurs while setting up the click listeners.
     */
    fun setupClickListeners(
        view: AppCompatActivity,
        context: Context,
        nameField: EditText,
        emailField: EditText,
        passwordField: EditText,
        updateButton: Button,
        deleteAccountButton: Button,
        logOutButton: Button,
        orderHistoryLayout: RelativeLayout
    ) {
        try {
            updateButton.setOnClickListener {
                val fullName = nameField.text.toString().trim()
                val nameParts = fullName.split(" ")
                val fName = nameParts.getOrNull(0) ?: ""
                val lName = nameParts.getOrNull(1) ?: ""
                if (!validateManager.isValidPassword(passwordField.text.toString())) {
                    notification.sendNotification("Password must be at least 6 characters long.", view)
                    return@setOnClickListener
                } else {
                    customerRepository.updateCustomer(
                        fName,
                        lName,
                        emailField.text.toString(),
                        passwordField.text.toString()
                    )
                    accountController.editAccountDetails(
                        view,
                        fName,
                        lName,
                        emailField.text.toString(),
                        passwordField.text.toString()
                    )
                }
            }
            deleteAccountButton.setOnClickListener {
                notification.sendDeleteAccountPrompt(context as AppCompatActivity) { confirmed ->
                    if (confirmed) {
                        accountController.deleteAccount(
                            view,
                            Volley.newRequestQueue(context),
                            BuildConfig.URL_DELETE_ACCOUNT,
                        )
                    }
                }
            }
            logOutButton.setOnClickListener {
                accountController.logout()
            }
            orderHistoryLayout.setOnClickListener {
                if (orderRepository.getAllOrders().isEmpty()) {
                    notification.sendNotification("No orders found", view)
                    return@setOnClickListener
                }
                else {
                    navigationController.navigateToFragment(OrderHistoryView(), R.id.fragment_container)
                }
            }
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading account page.", view)
            Log.d("AccountView", "Error: $e")
        }
    }
}