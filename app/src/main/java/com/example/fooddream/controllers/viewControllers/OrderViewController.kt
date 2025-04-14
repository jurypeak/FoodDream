package com.example.fooddream.controllers.viewControllers

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.R
import com.example.fooddream.adapters.OrderAdapter
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.OrderItem
import com.example.fooddream.repositories.OrderItemRepository
import com.example.fooddream.views.ProductView

/**
 * OrderViewController is responsible for managing the order view in the application.
 * It handles user interactions, updates order details, and manages navigation to product details.
 *
 * @property navigationController Controller for managing navigation actions.
 * @property notification Notification manager for displaying messages to the user.
 */
class OrderViewController(
    private val navigationController: NavigationController,
    private val notification: Notification,
) {

    /**
     * Handles the click event on a product in the order view.
     * It navigates to the product view with the selected product's ID.
     *
     * @param view The activity context.
     * @param order The order item that was clicked.
     *
     * @throws Exception if an error occurs while navigating to the product view.
     */
    fun onProductClick(
        view: AppCompatActivity,
        order: OrderItem,
    ) {
        try {
            val bundle = Bundle().apply {
                putInt("ProductId", order.getProductId())
            }
            val productViewFragment = ProductView().apply {
                arguments = bundle
            }
            navigationController.navigateToFragment(productViewFragment, R.id.fragment_container)
        } catch (e: Exception) {
            notification.sendNotification("Error while clicking on product in order view.", view)
            Log.e("OrderView", "Error on product click: ${e.message}")
        }
    }

    /**
     * Gets the order items for a specific order ID and updates the order list.
     *
     * @param view The activity context.
     * @param orderId The ID of the order to retrieve items for.
     * @param orderList The list to be updated with the retrieved order items.
     * @param orderItemRepository The repository for managing order items.
     * @param orderAdapter The adapter for displaying the order items.
     *
     * @throws Exception if an error occurs while retrieving order items.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addDataToList(
        view: AppCompatActivity,
        orderId: Int,
        orderList: ArrayList<OrderItem>,
        orderItemRepository: OrderItemRepository,
        orderAdapter: OrderAdapter,
    ) {
        try {
            val allOrderItems = ArrayList<OrderItem>()

            Log.d("OrderView", "Order ID: $orderId")
            val orderItem = orderItemRepository.getOrderItems(orderId)
            allOrderItems.addAll(orderItem)

            orderList.clear()
            orderList.addAll(allOrderItems)

            orderAdapter.notifyDataSetChanged()
        } catch (e: Exception) {
            notification.sendNotification("Error while gathering order items in order view.", view)
            Log.e("OrderView", "Error adding data to list: ${e.message}")
        }
    }
}