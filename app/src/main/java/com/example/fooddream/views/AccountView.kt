package com.example.fooddream.views

import CustomerRepository
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.OrderController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.repositories.BasketRepository
import com.example.fooddream.repositories.OrderRepository
import com.example.fooddream.repositories.PaymentRepository
import com.example.fooddream.utils.AccountManager
import com.example.fooddream.utils.OrderManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class AccountView : Fragment() {

    private lateinit var sessionController: SessionController
    private lateinit var customerRepository: CustomerRepository
    private lateinit var navigationController: NavigationController
    private lateinit var orderRepository: OrderRepository
    private lateinit var orderController: OrderController
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
    private var currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.account_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionController = SessionController(requireActivity() as AppCompatActivity)
        customerRepository = CustomerRepository(requireActivity() as AppCompatActivity)
        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        orderRepository = OrderRepository(requireActivity() as AppCompatActivity)
        orderController = OrderController(requireActivity() as AppCompatActivity)
        accountManager = AccountManager(requireActivity() as AppCompatActivity)
        orderManager = OrderManager(requireActivity() as AppCompatActivity, customerRepository)
        basketRepository = BasketRepository(requireActivity() as AppCompatActivity)
        paymentRepository = PaymentRepository(requireActivity() as AppCompatActivity)
        notification = Notification()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val accountId = customerRepository.getCustomer()?.getAccountId()
                if (accountId != null) {
                    orderManager.getOrders(
                        Volley.newRequestQueue(requireContext()),
                        BuildConfig.URL_GET_ORDERS,
                        accountId
                    )
                } else {

                }
                init(view)
            } catch (e: Exception) {
                init(view)
            }
        }
    }

    private fun init(view: View) {
        initializeViewComponents(view)
        setListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun initializeViewComponents(view: View) {
        orderHistoryLayout = view.findViewById(R.id.order_history_layout)
        emailField = view.findViewById(R.id.email_account)
        emailField.setText(customerRepository.getCustomer()?.getEmail())
        nameField = view.findViewById(R.id.name_account)
        nameField.setText(customerRepository.getCustomer()?.getFName() + " " + customerRepository.getCustomer()?.getLName())
        orderNumberField = view.findViewById(R.id.order_number)
        orderDateField = view.findViewById(R.id.date)
        orderTotalField = view.findViewById(R.id.total)
        val orders = orderRepository.getAllOrders()
        val payments = paymentRepository.getPayments()
        if (orders.isNotEmpty()) {
            orderNumberField.text = "Order #${orders.last().getOrderId()}"
            orderDateField.text = orders.last().getOrderDate()
            orderTotalField.text = currencyFormat.format(payments.last().getAmount())
        } else {
            orderNumberField.text = ""
            orderDateField.text = "No Orders"
            orderTotalField.text = ""
        }
        passwordField = view.findViewById(R.id.password_account)
        passwordField.setText(customerRepository.getCustomer()?.getPassword())
        updateButton = view.findViewById(R.id.updateButton)
        deleteAccountButton = view.findViewById(R.id.deleteButton)
        logOutButton = view.findViewById(R.id.logoutButton)
    }

    private fun setListeners() {
        updateButton.setOnClickListener {
            val fullName = nameField.text.toString().trim()
            val nameParts = fullName.split(" ")
            val fName = nameParts.getOrNull(0) ?: ""
            val lName = nameParts.getOrNull(1) ?: ""
            customerRepository.updateCustomer(
                fName,
                lName,
                emailField.text.toString(),
                passwordField.text.toString()
            )
            accountManager.updateAccount(
                emailField.text.toString(),
                fName,
                lName,
                passwordField.text.toString(),
                requireActivity() as AppCompatActivity,
                Volley.newRequestQueue(requireContext()),
                BuildConfig.URL_UPDATE_ACCOUNT
            )
        }
        deleteAccountButton.setOnClickListener {
            accountManager.deleteAccount(
                requireActivity() as AppCompatActivity,
                Volley.newRequestQueue(requireContext()),
                BuildConfig.URL_DELETE_ACCOUNT
            )
        }
        logOutButton.setOnClickListener {
            sessionController.clearUserSession()
            basketRepository.clearBasket()
            navigationController.navigateToActivity(LoginView::class.java)
        }
        orderHistoryLayout.setOnClickListener {
            if (orderRepository.getAllOrders().isEmpty()) {
                notification.sendNotification("No orders found", requireActivity() as AppCompatActivity)
                return@setOnClickListener
            }
            else {
                navigationController.navigateToFragment(OrderHistoryView(), R.id.fragment_container)
            }
        }
    }
}