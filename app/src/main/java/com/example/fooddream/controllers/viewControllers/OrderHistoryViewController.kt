package com.example.fooddream.controllers.viewControllers

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.R
import com.example.fooddream.adapters.OrderHistoryAdapter
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Order
import com.example.fooddream.models.Payment
import com.example.fooddream.repositories.OrderRepository
import com.example.fooddream.repositories.PaymentRepository
import com.example.fooddream.views.OrderView

/**
 * OrderHistoryViewController is responsible for managing the order history view in the application.
 * It handles user interactions, updates order history details, and manages navigation to order details.
 *
 * @property navigationController Controller for managing navigation actions.
 * @property notification Notification manager for displaying messages to the user.
 */
class OrderHistoryViewController(
    private val navigationController: NavigationController,
    private val notification: Notification,
) {

    /**
     * Sets up click listeners for the order history view.
     *
     * @param orderHistoryAdapter Adapter for displaying order history.
     * @param orderHistoryList List of orders to be displayed in the order history.
     * @param paymentList List of payments associated with the orders.
     * @param orderRepository Repository for managing order data.
     * @param paymentRepository Repository for managing payment data.
     * @param view The activity context.
     *
     * @throws Exception if an error occurs while setting up click listeners.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addDataToList(
        view: AppCompatActivity,
        orderHistoryList: ArrayList<Order>,
        paymentList: ArrayList<Payment>,
        orderRepository: OrderRepository,
        paymentRepository: PaymentRepository,
        orderHistoryAdapter: OrderHistoryAdapter
    ) {
        try {
            orderHistoryList.clear()
            orderHistoryList.addAll(orderRepository.getAllOrders())

            paymentList.clear()
            paymentList.addAll(paymentRepository.getPayments())

            orderHistoryAdapter.notifyDataSetChanged()
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading orders on order history page.", view)
            Log.d("OrderHistoryView", "Error: $e")
        }
    }

    /**
     * Handles the click event on an order item in the order history.
     *
     * @param order The selected order.
     */
    fun onOrderClick(
        order: Order,
    ) {
        val bundle = Bundle().apply {
            putInt("orderId", order.getOrderId())
        }
        val orderViewFragment = OrderView().apply {
            arguments = bundle
        }
        navigationController.navigateToFragment(orderViewFragment, R.id.fragment_container)
    }
}