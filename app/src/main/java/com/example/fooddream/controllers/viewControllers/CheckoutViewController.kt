package com.example.fooddream.controllers.viewControllers

import android.content.Context
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.R
import com.example.fooddream.controllers.OrderController
import com.example.fooddream.messengers.Notification

/**
 * CheckoutViewController is responsible for managing the view and interactions
 * related to the checkout process in the application.
 *
 * @property paymentItem The selected payment method.
 * @property autoCompleteTextView The AutoCompleteTextView for selecting payment methods.
 * @property adapterItems The ArrayAdapter for populating the AutoCompleteTextView.
 */
class CheckoutViewController() {
    private var paymentItem = ""
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private val paymentItems = listOf("Debit/Credit Card", "PayPal", "Google Pay")
    private lateinit var adapterItems: ArrayAdapter<String>

    /**
     * Initializes the checkout screen by setting up the AutoCompleteTextView with payment methods.
     *
     * @param view The activity where the checkout screen is displayed.
     * @param context The context of the application.
     * @param notification The notification manager for displaying messages to the user.
     *
     * @throws Exception if an error occurs while initializing the checkout screen.
     */
    fun initializeCheckoutScreen(
        view: AppCompatActivity,
        context: Context,
        notification: Notification
    ) {
        try {
            autoCompleteTextView = view.findViewById(R.id.auto_complete_text)
            adapterItems = ArrayAdapter<String>(context, R.layout.list_item, paymentItems)

            autoCompleteTextView.setAdapter(adapterItems)
        } catch (e: Exception) {
            notification.sendNotification("Error while loading checkout page.", view)
            Log.e("CheckoutView", "Error initializing AutoCompleteTextView: ${e.message}")
        }
    }

    /**
     * Sets up click listeners for the checkout button and payment method selection.
     *
     * @param view The activity where the checkout screen is displayed.
     * @param orderController The controller for managing order-related actions.
     * @param notification The notification manager for displaying messages to the user.
     * @param payButton The button to initiate the payment process.
     * @param emailView EditText for entering the email address.
     * @param nameView EditText for entering the name.
     * @param addressView EditText for entering the address.
     * @param postcodeView EditText for entering the postcode.
     * @param townView EditText for entering the town.
     *
     * @throws Exception if an error occurs while setting up click listeners.
     */
    fun setupClickListeners(
        view: AppCompatActivity,
        orderController: OrderController,
        notification: Notification,
        payButton: Button,
        emailView: EditText,
        nameView: EditText,
        addressView: EditText,
        postcodeView: EditText,
        townView: EditText,
    ) {
        try {
            autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                paymentItem = parent.getItemAtPosition(position).toString()
            }
            payButton.setOnClickListener {
                orderController.startOrder(
                    emailView,
                    nameView,
                    addressView,
                    postcodeView,
                    townView,
                    paymentItem,
                    view
                )
            }
        } catch (e: Exception) {
            notification.sendNotification("Error while loading checkout page.", view)
            Log.e("CheckoutView", "Error setting listeners: ${e.message}")
        }
    }
}