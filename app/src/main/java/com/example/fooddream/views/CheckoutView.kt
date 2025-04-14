package com.example.fooddream.views

import CustomerRepository
import android.annotation.SuppressLint
import android.icu.text.NumberFormat
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.R
import com.example.fooddream.controllers.viewControllers.CheckoutViewController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.OrderController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.repositories.BasketRepository
import java.util.Locale

/**
 * CheckoutView is a Fragment that displays the checkout screen for the user to enter their details
 * and proceed with the payment.
 *
 * @constructor Creates an instance of CheckoutView.
 *
 * @property navigationController Manages navigation actions.
 * @property orderController Manages order-related actions.
 * @property customerRepository Handles customer data operations.
 * @property checkoutViewController Controller for managing the checkout view logic.
 * @property notification Notification manager for displaying messages to the user.
 * @property basketRepository Handles basket data operations.
 * @property itemCountTextView TextView for displaying the number of items in the basket.
 * @property totalPriceTextView TextView for displaying the total price of items in the basket.
 * @property emailView EditText for entering the user's email.
 * @property nameView EditText for entering the user's name.
 * @property addressView EditText for entering the user's address.
 * @property postcodeView EditText for entering the user's postcode.
 * @property townView EditText for entering the user's town.
 * @property paymentItem String representing the selected payment method.
 * @property payButton Button for proceeding with payment.
 */
class CheckoutView : Fragment() {

    private lateinit var navigationController: NavigationController
    private lateinit var orderController: OrderController
    private lateinit var customerRepository: CustomerRepository
    private lateinit var checkoutViewController: CheckoutViewController
    private lateinit var notification: Notification
    private lateinit var basketRepository: BasketRepository
    private lateinit var itemCountTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var emailView: EditText
    private lateinit var nameView: EditText
    private lateinit var addressView: EditText
    private lateinit var postcodeView: EditText
    private lateinit var townView: EditText
    private lateinit var paymentItem: String
    private lateinit var payButton: Button

    /**
     * Currency format for displaying prices in the UK locale.
     */
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.checkout_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    /**
     * Initializes the CheckoutView by setting up the controllers, view components, and UI actions.
     *
     * @param view The root view of the fragment.
     *
     * @throws Exception if an error occurs while initializing the view components or setting up the UI actions.
     */
    private fun init(view: View) {
        initializeControllers(requireActivity() as AppCompatActivity)
        initializeViewComponents(view)
        checkoutViewController.initializeCheckoutScreen(
            requireActivity() as AppCompatActivity,
            requireContext(),
            notification
        )
        setUIActions()
    }

    /**
     * Initializes the controllers used in the CheckoutView.
     *
     * @param view The activity context.
     *
     * @throws Exception if an error occurs while initializing the controllers.
     */
    private fun initializeControllers(view: AppCompatActivity) {
        try {
            navigationController = NavigationController(view)
            orderController = OrderController(view)
            basketRepository = BasketRepository(view)
            customerRepository = CustomerRepository(view)
            notification = Notification()

            checkoutViewController = CheckoutViewController()
        } catch (e: Exception) {
            Log.e("CheckoutView", "Error initializing controllers: ${e.message}")
            notification.sendNotification("Error occurred while loading the checkout page.", requireActivity() as AppCompatActivity)
        }
    }

    /**
     * Sets up the UI actions for the checkout view.
     * This method configures click listeners for various UI components.
     *
     * @throws Exception if an error occurs while setting up UI actions.
     */
    private fun setUIActions() {
        try {
            checkoutViewController.setupClickListeners(
                requireActivity() as AppCompatActivity,
                orderController,
                notification,
                payButton,
                emailView,
                nameView,
                addressView,
                postcodeView,
                townView,
            )
        } catch (e: Exception) {
            notification.sendNotification("Error while loading checkout page.", requireActivity() as AppCompatActivity)
            Log.e("CheckoutView", "Error setting UI actions: ${e.message}")
        }
    }

    /**
     * Initializes the view components used in the CheckoutView.
     *
     * @param view The root view of the fragment.
     *
     * @throws Exception if an error occurs while initializing the view components.
     */
    @SuppressLint("SetTextI18n")
    private fun initializeViewComponents(view: View) {
        try {
            itemCountTextView = view.findViewById(R.id.itemQuantityHeader)
            itemCountTextView.text = "${basketRepository.getBasketSize(customerRepository.getCustomer()?.getAccountId())} Items"

            totalPriceTextView = view.findViewById(R.id.priceText)
            totalPriceTextView.text = currencyFormat.format(basketRepository.getBasketTotalPrice(customerRepository.getCustomer()?.getAccountId()))

            paymentItem = ""
            emailView = view.findViewById(R.id.email_checkout)
            nameView = view.findViewById(R.id.name_checkout)
            addressView = view.findViewById(R.id.address_checkout)
            payButton = view.findViewById(R.id.payButton)
            postcodeView = view.findViewById(R.id.postcode_checkout)
            townView = view.findViewById(R.id.town_checkout)
        } catch (e: Exception) {
            notification.sendNotification("Error while loading checkout page.", requireActivity() as AppCompatActivity)
            Log.e("CheckoutView", "Error initializing view components: ${e.message}")
        }
    }
}