package com.example.fooddream.controllers.viewControllers

import CustomerRepository
import android.annotation.SuppressLint
import android.icu.text.NumberFormat
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.R
import com.example.fooddream.adapters.BasketAdapter
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.BasketItem
import com.example.fooddream.repositories.BasketRepository
import com.example.fooddream.views.CheckoutView
import com.example.fooddream.views.ProductView
import java.util.Locale

/**
 * BasketViewController is responsible for managing the basket view in the application.
 * It handles user interactions, product management, and navigation within the basket.
 *
 * @property navigationController Controller for managing navigation actions.
 * @property notification Notification manager for displaying messages to the user.
 */
class BasketViewController(
    private val navigationController: NavigationController,
    private val notification: Notification,
) {

    /**
     * NumberFormat instance for formatting currency values.
     */
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK)

    /**
     * Sets up click listeners for the checkout button and other UI elements.
     *
     * @param checkoutButton The button to set the click listener on.
     * @param basketRepository Repository for managing basket data.
     * @param customerRepository Repository for managing customer data.
     * @param view The activity context.
     *
     * @throws Exception if an error occurs while setting up click listeners.
     */
    fun setupClickListeners(
        checkoutButton: Button,
        basketRepository: BasketRepository,
        customerRepository: CustomerRepository,
        view: AppCompatActivity
    ) {
        try {
            checkoutButton.setOnClickListener {
                if (basketRepository.getBasketSize(customerRepository.getCustomer()?.getAccountId()) == 0) {
                    notification.sendNotification("Basket is empty.", view)
                    notification.sendNotification("Please add items to your basket before proceeding to checkout.", view)
                    return@setOnClickListener
                }
                else {
                    navigationController.navigateToFragment(CheckoutView(), R.id.fragment_container)
                }
            }
        } catch (e: Exception) {
            notification.sendNotification("Error while loading in basket page.", view)
            Log.d("BasketView", "Error setting listeners: $e")
        }
    }

    /**
     * Adds data to the basket list and notifies the adapter of changes.
     *
     * @param basketList The list of basket items.
     * @param basketAdapter The adapter for the basket list.
     * @param basketRepository Repository for managing basket data.
     * @param customerRepository Repository for managing customer data.
     * @param view The activity context.
     *
     * @throws Exception if an error occurs while adding data to the list.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addDataToList(
        basketList: ArrayList<BasketItem>,
        basketAdapter: BasketAdapter,
        basketRepository: BasketRepository,
        customerRepository: CustomerRepository,
        view: AppCompatActivity
    ) {
        try {
            basketList.addAll(basketRepository.getAllBasketItems(customerRepository.getCustomer()?.getAccountId()))
            basketAdapter.notifyDataSetChanged()
        } catch (e: Exception) {
            notification.sendNotification("Error while loading basket items.", view)
            Log.d("BasketView", "Error adding data to list: $e")
        }
    }

    /**
     * Handles the click event for a product in the basket.
     *
     * @param basketItem The clicked basket item.
     */
    fun onProductClick(basketItem: BasketItem) {
        Log.d(
            "RecyclerViewClick",
            "Clicked product: ${basketItem.getItemName()} with ID: ${basketItem.getProductId()}"
        )
        val bundle = Bundle().apply {
            putInt("ProductId", basketItem.getProductId())
        }
        val productViewFragment = ProductView().apply {
            arguments = bundle
        }
        navigationController.navigateToFragment(
            productViewFragment,
            R.id.fragment_container
        )
    }


    /** Handles adding products to the basket.
     *
     * @param basketItem The basket item to be modified.
     * @param itemCountTextView The TextView displaying the item count.
     * @param totalPriceTextView The TextView displaying the total price.
     * @param basketRepository Repository for managing basket data.
     * @param customerRepository Repository for managing customer data.
     * @param view The activity context.
     */
    fun onAddToBasket(
        basketItem: BasketItem,
        itemCountTextView: TextView,
        totalPriceTextView: TextView,
        basketRepository: BasketRepository,
        customerRepository: CustomerRepository,
        view: AppCompatActivity
    ) {
        Log.d("AddToBasket", "Added product: ${basketItem.getItemName()} to the basket")
        updateHeaderInfo(
            itemCountTextView,
            totalPriceTextView,
            basketRepository,
            customerRepository,
            view
        )
    }

    /**
     * Handles removing products from the basket.
     *
     * @param basketItem The basket item to be removed.
     * @param itemCountTextView The TextView displaying the item count.
     * @param totalPriceTextView The TextView displaying the total price.
     * @param basketRepository Repository for managing basket data.
     * @param customerRepository Repository for managing customer data.
     * @param view The activity context.
     */
    fun onRemoveFromBasket(
        basketItem: BasketItem,
        itemCountTextView: TextView,
        totalPriceTextView: TextView,
        basketRepository: BasketRepository,
        customerRepository: CustomerRepository,
        view: AppCompatActivity
    ) {
        Log.d("RemoveFromBasket", "Removed product: ${basketItem.getItemName()} from the basket")
        updateHeaderInfo(
            itemCountTextView,
            totalPriceTextView,
            basketRepository,
            customerRepository,
            view
        )
    }

    /**
     * Handles incrementing the quantity of a product in the basket.
     *
     * @param basketItem The basket item to be modified.
     * @param itemCountTextView The TextView displaying the item count.
     * @param totalPriceTextView The TextView displaying the total price.
     * @param basketRepository Repository for managing basket data.
     * @param customerRepository Repository for managing customer data.
     * @param view The activity context.
     */
    fun onIncrementQuantity(
        basketItem: BasketItem,
        itemCountTextView: TextView,
        totalPriceTextView: TextView,
        basketRepository: BasketRepository,
        customerRepository: CustomerRepository,
        view: AppCompatActivity
    ) {
        Log.d("IncrementQuantity", "Incremented product quantity: ${basketItem.getItemName()} in the basket")
        updateHeaderInfo(
            itemCountTextView,
            totalPriceTextView,
            basketRepository,
            customerRepository,
            view
        )
    }

    /**
     * Updates the header information displaying the number of items and total price in the basket.
     *
     * @param itemCountTextView The TextView displaying the item count.
     * @param totalPriceTextView The TextView displaying the total price.
     * @param basketRepository Repository for managing basket data.
     * @param customerRepository Repository for managing customer data.
     * @param view The activity context.
     */
    @SuppressLint("SetTextI18n")
    fun updateHeaderInfo(
        itemCountTextView: TextView,
        totalPriceTextView: TextView,
        basketRepository: BasketRepository,
        customerRepository: CustomerRepository,
        view: AppCompatActivity
    ) {
        try {
            itemCountTextView.text = "${basketRepository.getBasketSize(customerRepository.getCustomer()?.getAccountId())} Items"
            totalPriceTextView.text = currencyFormat.format(basketRepository.getBasketTotalPrice(customerRepository.getCustomer()?.getAccountId()))
        } catch (e: Exception) {
            notification.sendNotification("Error while getting number of items & total price of items in basket.", view)
            Log.d("BasketView", "Error updating header info: $e")
        }
    }
}