package com.example.fooddream.controllers

import CustomerRepository
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Address
import com.example.fooddream.models.Order
import com.example.fooddream.models.Payment
import com.example.fooddream.repositories.BasketRepository
import com.example.fooddream.utils.OrderManager

/**
 * OrderController is responsible for handling the order process in the application.
 * It validates user input, manages order details, and interacts with the OrderManager to process orders.
 *
 * @param view The AppCompatActivity context for displaying notifications and managing UI interactions.
 */
class OrderController(view: AppCompatActivity) {

    /**
     * Order model to hold order details.
     */
    private val orderModel = Order(
        fName = "",
        lName = "",
        email = "",
        accountId = 0,
        orderId = 0,
        orderDate = "",
    )
    /**
     * Address model to hold address details.
     */
    private val addressModel = Address(
        addressId = 0,
        orderId = 0,
        street = "",
        postcode = "",
        town = ""
    )
    /**
     * Payment model to hold payment details.
     */
    private val paymentModel = Payment(
        paymentId = 0,
        orderId = 0,
        paymentMethod = "",
        paymentDate = "",
        amount = 0.00
    )

    // Repositories and managers for handling data and operations
    private val basketRepository = BasketRepository(view)
    private val customerRepository = CustomerRepository(view)
    private val orderManager = OrderManager(view, CustomerRepository(view))
    private val notification = Notification()

    /**
     * Starts the order process by validating user input and initiating the order handling.
     *
     * @param emailField The EditText for entering the email address.
     * @param nameField The EditText for entering the name.
     * @param addressField The EditText for entering the address.
     * @param postcodeField The EditText for entering the postcode.
     * @param townField The EditText for entering the town.
     * @param payment The selected payment method.
     * @param view The AppCompatActivity context for displaying notifications and managing UI interactions.
     *
     * @throws Exception if an error occurs while starting the order process.
     */
    fun startOrder(
        emailField: EditText,
        nameField: EditText,
        addressField: EditText,
        postcodeField: EditText,
        townField: EditText,
        payment: String,
        view: AppCompatActivity
    ) {
        try {
            val email = emailField.text.toString()
            val name = nameField.text.toString()
            val address = addressField.text.toString()
            val postcode = postcodeField.text.toString()
            val town = townField.text.toString()

            if (email.isBlank() || name.isBlank() || address.isBlank()
                || postcode.isBlank() || town.isBlank() || payment.isBlank()) {
                notification.sendNotification("All fields need to be entered.", view)
            }

            else if (isValidPostcode(postcode) == false) {
                notification.sendNotification("Postcode incorrect.", view)
            }

            else {
                val nameParts = name.split(" ")
                val fName = nameParts.getOrNull(0) ?: ""
                val lName = nameParts.getOrNull(1) ?: ""

                orderModel.setFName(fName)
                orderModel.setLName(lName)
                orderModel.setEmail(email)

                addressModel.setStreet(address)
                addressModel.setTown(town)
                addressModel.setPostcode(postcode)

                paymentModel.setPaymentMethod(payment)
                paymentModel.setAmount(basketRepository.getBasketTotalPrice(customerRepository.getCustomer()?.getAccountId()))

                orderManager.handleOrder(
                    email,
                    fName,
                    lName,
                    address,
                    town,
                    postcode,
                    payment,
                    Volley.newRequestQueue(view),
                    BuildConfig.URL_ORDER,
                    BuildConfig.URL_PAYMENT,
                    BuildConfig.URL_ADDRESS,
                    BuildConfig.URL_ORDER_ITEMS
                )
            }
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while registering", view)
            Log.d("Register Handling Error", "$error")
        }
    }

    //https://youtu.be/NBL0igWs2YU


    /**
     * Validates the postcode format.
     * This method uses a regular expression to check if the postcode is a valid UK postcode.
     *
     * @param postcode The postcode to validate.
     * @return True if the postcode is valid, false otherwise.
     *
     * @throws Exception if an error occurs while validating the postcode.
     */
    private fun isValidPostcode(postcode: String): Boolean {
        try {
            val cleanedPostcode = postcode.replace("\\s".toRegex(), "")

            val regex = Regex("^([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([AZa-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9]?[A-Za-z]))))[0-9][A-Za-z]{2})\$")
            return regex.matches(cleanedPostcode)
        } catch (e: Exception) {
            Log.e("OrderController", "Error validating postcode: ${e.message}")
            return false
        }
    }
}