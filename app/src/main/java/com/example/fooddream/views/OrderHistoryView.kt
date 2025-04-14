package com.example.fooddream.views

import CustomerRepository
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddream.R
import com.example.fooddream.adapters.OrderHistoryAdapter
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.viewControllers.OrderHistoryViewController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Order
import com.example.fooddream.models.Payment
import com.example.fooddream.repositories.OrderRepository
import com.example.fooddream.repositories.PaymentRepository

/**
 * OrderHistoryView is a Fragment that displays the user's order history.
 * It allows the user to view details of their past orders and payments.
 *
 * @property recyclerView The RecyclerView that displays the list of orders.
 * @property orderHistoryList The list of orders to be displayed in the RecyclerView.
 * @property paymentList The list of payments associated with the orders.
 * @property orderHistoryAdapter The adapter for the RecyclerView.
 * @property navigationController The controller for managing navigation between views.
 * @property sessionController The controller for managing user sessions.
 * @property customerRepository The repository for managing customer data.
 * @property orderRepository The repository for managing order data.
 * @property paymentRepository The repository for managing payment data.
 * @property orderHistoryController The controller for managing the order history view.
 * @property notification The notification manager for displaying messages to the user.
 */
class OrderHistoryView : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderHistoryList: ArrayList<Order>
    private lateinit var paymentList: ArrayList<Payment>
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter
    private lateinit var navigationController: NavigationController
    private lateinit var sessionController: SessionController
    private lateinit var customerRepository: CustomerRepository
    private lateinit var orderRepository: OrderRepository
    private lateinit var paymentRepository: PaymentRepository
    private lateinit var orderHistoryController: OrderHistoryViewController
    private lateinit var notification: Notification

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.order_history_page, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        notification = Notification()

        init(view)
    }

    /**
     * Initializes the OrderHistoryView by setting up the controllers, view components,
     * and the RecyclerView for displaying orders.
     *
     * @param view The root view of the fragment.
     */
    private fun init(view: View) {
        initializeControllers()
        initializeRecycler(view)
    }

    /**
     * Initializes the RecyclerView and its adapter.
     *
     * @param view The root view of the fragment.
     *
     * This method initializes the RecyclerView, sets its layout manager,
     * and creates an adapter for displaying the order history.
     *
     * @throws Exception if an error occurs during initialization.
     */
    private fun initializeRecycler(view: View) {
        try {
            recyclerView = view.findViewById(R.id.order_history_view)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(requireActivity() as AppCompatActivity, 1)

            orderHistoryList = ArrayList()

            paymentList = ArrayList()

            orderHistoryAdapter = OrderHistoryAdapter(
                requireActivity() as AppCompatActivity,
                orderHistoryList,
            ) { order -> orderHistoryController.onOrderClick(order) }
            recyclerView.adapter = orderHistoryAdapter

            orderHistoryController = OrderHistoryViewController(
                navigationController,
                notification
            )

            orderHistoryController.addDataToList(
                requireActivity() as AppCompatActivity,
                orderHistoryList,
                paymentList,
                orderRepository,
                paymentRepository,
                orderHistoryAdapter,
            )
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading orders on order history page.", requireActivity() as AppCompatActivity)
            Log.d("OrderHistoryView", "Error: $e")
        }
    }

    /**
     * Initializes the controllers used in the OrderHistoryView.
     *
     * @throws Exception if an error occurs while initializing the controllers.
     */
    private fun initializeControllers() {
        try {
            sessionController = SessionController(requireActivity() as AppCompatActivity)
            customerRepository = CustomerRepository(requireActivity() as AppCompatActivity)
            orderRepository = OrderRepository(requireActivity() as AppCompatActivity)
            paymentRepository = PaymentRepository(requireActivity() as AppCompatActivity)
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading order history page.", requireActivity() as AppCompatActivity)
            Log.d("OrderHistoryView", "Error: $e")
        }
    }
}