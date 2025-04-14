package com.example.fooddream.views

import CustomerRepository
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.R
import com.example.fooddream.controllers.AccountController
import com.example.fooddream.controllers.viewControllers.AccountViewController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.OrderController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.repositories.BasketRepository
import com.example.fooddream.repositories.OrderRepository
import com.example.fooddream.repositories.PaymentRepository
import com.example.fooddream.utils.AccountManager
import com.example.fooddream.utils.OrderManager
import java.text.NumberFormat
import java.util.Locale

/**
 * AccountView is a Fragment that displays the user's account information and allows them to update their details,
 * view order history, and manage their account settings.
 *
 * @constructor Creates an instance of AccountView.
 *
 * @property sessionController Manages user session information.
 * @property customerRepository Handles customer data operations.
 * @property navigationController Manages navigation actions.
 * @property orderRepository Handles order data operations.
 * @property orderController Manages order-related actions.
 * @property accountManager Manages account-related actions.
 * @property orderManager Manages order-related actions.
 * @property basketRepository Handles basket data operations.
 * @property paymentRepository Handles payment data operations.
 * @property emailField EditText for entering the user's email.
 * @property nameField EditText for entering the user's name.
 * @property passwordField EditText for entering the user's password.
 * @property orderNumberField TextView for displaying the order number.
 * @property orderDateField TextView for displaying the order date.
 * @property orderTotalField TextView for displaying the order total.
 * @property updateButton Button for updating account information.
 * @property deleteAccountButton Button for deleting the account.
 * @property logOutButton Button for logging out of the account.
 * @property orderHistoryLayout RelativeLayout for displaying order history.
 * @property notification Notification manager for displaying messages to the user.
 * @property accountViewController Controller for managing the account view logic.
 * @property currencyFormat NumberFormat for formatting currency values.
 */
class AccountView : Fragment() {

    private lateinit var sessionController: SessionController
    private lateinit var customerRepository: CustomerRepository
    private lateinit var navigationController: NavigationController
    private lateinit var orderRepository: OrderRepository
    private lateinit var orderController: OrderController
    private lateinit var accountController: AccountController
    private lateinit var accountManager: AccountManager
    private lateinit var orderManager: OrderManager
    private lateinit var basketRepository: BasketRepository
    private lateinit var paymentRepository: PaymentRepository
    private lateinit var emailField: EditText
    private lateinit var nameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var orderNumberField: TextView
    private lateinit var orderDateField: TextView
    private lateinit var orderTotalField: TextView
    private lateinit var updateButton: Button
    private lateinit var deleteAccountButton: Button
    private lateinit var logOutButton: Button
    private lateinit var orderHistoryLayout: RelativeLayout
    private lateinit var notification: Notification
    private lateinit var accountViewController: AccountViewController
    private var currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.account_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    /**
     * Initializes the AccountView by setting up the view components, UI actions, and controllers.
     *
     * @param view The root view of the fragment.
     *
     * @throws Exception if an error occurs during initialization.
     */
    private fun init(view: View) {
        initializeControllers(requireActivity() as AppCompatActivity)
        initializeViewComponents(view)
        accountViewController.initializeAccountScreen(
            requireContext(),
            requireActivity() as AppCompatActivity,
            orderManager
        )
        setUIActions()
    }

    /**
     * Initializes the controllers used in the AccountView.
     * This method is responsible for creating instances of the necessary controllers and repositories.
     *
     * @param view The activity context used to initialize the controllers.
     *
     * @throws Exception if an error occurs while initializing the controllers.
     */
    private fun initializeControllers(view: AppCompatActivity) {
        try {
            sessionController = SessionController(view)
            navigationController = NavigationController(view)
            orderController = OrderController(view)
            customerRepository = CustomerRepository(view)
            orderRepository = OrderRepository(view)
            basketRepository = BasketRepository(view)
            paymentRepository = PaymentRepository(view)
            accountManager = AccountManager(view)
            accountController = AccountController(view)
            orderManager = OrderManager(view, customerRepository)
            notification = Notification()
            accountViewController = AccountViewController(
                customerRepository,
                orderRepository,
                accountController,
                navigationController,
                notification
            )
        } catch (e: Exception) {
            Log.e("AccountView", "Error initializing controllers: ${e.message}")
            notification.sendNotification("Error occurred while loading account page.", view)
        }
    }

    /**
     * Sets up the UI actions for the AccountView.
     * This method is responsible for setting up click listeners for various UI components.
     *
     * @throws Exception if an error occurs while setting up the UI actions.
     */
    private fun setUIActions() {
        try {
            accountViewController.setupClickListeners(
                requireActivity() as AppCompatActivity,
                requireContext(),
                nameField,
                emailField,
                passwordField,
                updateButton,
                deleteAccountButton,
                logOutButton,
                orderHistoryLayout,
            )
        } catch (e: Exception) {
            Log.e("AccountView", "Error setting UI actions: ${e.message}")
            notification.sendNotification("Error occurred while loading account page.", requireActivity() as AppCompatActivity)
        }
    }

    /**
     * Initializes the view components for the AccountView.
     * This method is responsible for finding and assigning the views to their respective variables.
     *
     * @param view The root view of the fragment.
     *
     * @throws Exception if an error occurs while initializing the view components.
     */
    @SuppressLint("SetTextI18n")
    private fun initializeViewComponents(view: View) {
        try {
            orderHistoryLayout = view.findViewById(R.id.order_history_layout)
            emailField = view.findViewById(R.id.email_account)
            nameField = view.findViewById(R.id.name_account)
            orderNumberField = view.findViewById(R.id.order_number)
            orderDateField = view.findViewById(R.id.date)
            orderTotalField = view.findViewById(R.id.total)
            passwordField = view.findViewById(R.id.password_account)
            updateButton = view.findViewById(R.id.updateButton)
            deleteAccountButton = view.findViewById(R.id.deleteButton)
            logOutButton = view.findViewById(R.id.logoutButton)

            passwordField.setText(customerRepository.getCustomer()?.getPassword())
            nameField.setText(customerRepository.getCustomer()?.getFName() + " " + customerRepository.getCustomer()?.getLName())
            emailField.setText(customerRepository.getCustomer()?.getEmail())

            accountViewController.checkIfOrdersExist(
                currencyFormat,
                orderNumberField,
                orderDateField,
                orderTotalField,
                paymentRepository.getPayments(),
                orderRepository.getAllOrders()
            )
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading account page.", requireActivity() as AppCompatActivity)
            Log.d("AccountView", "Error: $e")
        }
    }
}