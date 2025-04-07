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

class OrderController(view: AppCompatActivity) {
    
    private val orderModel = Order(
        fName = "",
        lName = "",
        email = "",
        accountId = 0,
        guestId = 0,
        orderId = 0,
        products = mutableListOf()
    )
    private val addressModel = Address(
        addressId = 0,
        orderId = 0,
        street = "",
        postcode = "",
        town = ""
    )
    private val paymentModel = Payment(
        paymentId = 0,
        orderId = 0,
        paymentMethod = "",
        amount = 0.00
    )

    private val basketRepository = BasketRepository(view)
    private val orderManager = OrderManager(view, CustomerRepository(view))
    private val notification = Notification()

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
                paymentModel.setAmount(basketRepository.getBasketTotalPrice())

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

    private fun isValidPostcode(postcode: String): Boolean {

        val cleanedPostcode = postcode.replace("\\s".toRegex(), "")

        val regex = Regex("^([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([AZa-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9]?[A-Za-z]))))[0-9][A-Za-z]{2})\$")
        return regex.matches(cleanedPostcode)
    }
}