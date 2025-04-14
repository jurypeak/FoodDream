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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddream.R
import com.example.fooddream.adapters.BasketAdapter
import com.example.fooddream.controllers.viewControllers.BasketViewController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.BasketItem
import com.example.fooddream.repositories.BasketRepository
import java.util.Locale

/**
 * BasketView is a Fragment that displays the user's shopping basket.
 * It allows the user to view, add, remove, and update items in the basket.
 *
 * @property recyclerView The RecyclerView that displays the list of basket items.
 * @property basketList The list of items in the basket.
 * @property basketAdapter The adapter for the RecyclerView.
 * @property navigationController The controller for managing navigation between views.
 * @property basketRepository The repository for managing basket data.
 * @property customerRepository The repository for managing customer data.
 * @property basketViewController The controller for managing the basket view.
 * @property notification The notification manager for displaying messages to the user.
 * @property itemCountTextView The TextView that displays the number of items in the basket.
 * @property totalPriceTextView The TextView that displays the total price of items in the basket.
 * @property checkoutButton The Button for proceeding to checkout.
 */
class BasketView : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var basketList: ArrayList<BasketItem>
    private lateinit var basketAdapter: BasketAdapter
    private lateinit var navigationController: NavigationController
    private lateinit var basketRepository: BasketRepository
    private lateinit var customerRepository: CustomerRepository
    private lateinit var basketViewController: BasketViewController
    private lateinit var notification: Notification
    private lateinit var itemCountTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var checkoutButton: Button

    /**
     * Currency format for displaying prices in the UK locale.
     */
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.basket_page, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    /**
     * Initializes the BasketView by setting up the necessary controllers, view components, and UI actions.
     *
     * @param view The root view of the fragment.
     *
     * This method is called after the view is created to ensure that all UI elements are ready for interaction.
     * It initializes the controllers, view components, and RecyclerView,
     * and sets up click listeners for UI actions.
     *
     * @throws Exception if an error occurs while initializing the view.
     */
    private fun init(view: View) {
        initializeControllers(requireActivity() as AppCompatActivity)
        initializeViewComponents(view)
        initializeRecycler(view)
        setupUIActions()
    }

    /**
     * Initializes the controllers used in the BasketView.
     *
     * @param view The activity context.
     *
     * This method attempts to create instances of the NavigationController,
     * BasketRepository, CustomerRepository, and Notification classes.
     * If an exception occurs, it sends a notification to the user and logs the error.
     *
     * @throws Exception if an error occurs while initializing the controllers.
     */
    private fun initializeControllers(view: AppCompatActivity) {
        try {
            navigationController = NavigationController(view)
            customerRepository = CustomerRepository(view)
            basketRepository = BasketRepository(view)
            notification = Notification()
        } catch (e: Exception) {
            Log.e("BasketView", "Error initializing controllers: ${e.message}")
            notification.sendNotification("Error occurred while loading the basket page.", view)
        }
    }

    /**
     * Initializes the RecyclerView and its adapter for displaying basket items.
     *
     * @param view The root view of the fragment.
     *
     * This method sets up the RecyclerView with a GridLayoutManager and initializes the BasketAdapter.
     * It also populates the basket list with data from the BasketRepository.
     *
     * @throws Exception if an error occurs while initializing the RecyclerView or its adapter.
     */
    private fun initializeRecycler(view: View) {
        try {
            recyclerView = view.findViewById(R.id.basket_view)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(requireActivity() as AppCompatActivity, 1)

            basketList = ArrayList()

            basketAdapter = BasketAdapter(
                requireActivity() as AppCompatActivity,
                basketList,
                { basketItem -> basketViewController.onProductClick(basketItem) },
                { basketItem -> basketViewController.onAddToBasket(
                    basketItem,
                    itemCountTextView,
                    totalPriceTextView,
                    basketRepository,
                    customerRepository,
                    requireActivity() as AppCompatActivity
                ) },
                { basketItem -> basketViewController.onRemoveFromBasket(
                    basketItem,
                    itemCountTextView,
                    totalPriceTextView,
                    basketRepository,
                    customerRepository,
                    requireActivity() as AppCompatActivity
                ) },
                { basketItem -> basketViewController.onIncrementQuantity(
                    basketItem,
                    itemCountTextView,
                    totalPriceTextView,
                    basketRepository,
                    customerRepository,
                    requireActivity() as AppCompatActivity
                ) }
            )
            recyclerView.adapter = basketAdapter

            basketViewController = BasketViewController(
                navigationController,
                notification,
            )

            basketViewController.addDataToList(
                basketList,
                basketAdapter,
                basketRepository,
                customerRepository,
                requireActivity() as AppCompatActivity,
            )

        } catch (e: Exception) {
            notification.sendNotification("Error while gathering basket items in basket.", requireActivity() as AppCompatActivity)
            Log.d("BasketView", "Error initializing RecyclerView: $e")
        }
    }

    /**
     * Sets up the UI actions for the basket view, including click listeners for buttons.
     *
     * This method is called after initializing the view components to ensure that
     * all UI elements are ready for interaction.
     *
     * @throws Exception if an error occurs while setting up UI actions.
     */
    private fun setupUIActions() {
        try {
            basketViewController.setupClickListeners(
                checkoutButton,
                basketRepository,
                customerRepository,
                requireActivity() as AppCompatActivity
            )
        } catch (e: Exception) {
            notification.sendNotification("Error while setting up basket page.", requireActivity() as AppCompatActivity)
            Log.d("BasketView", "Error setting up UI actions: $e")
        }
    }

    /**
     * Initializes the view components used in the BasketView.
     *
     * @param view The root view of the fragment.
     *
     * @throws Exception if an error occurs while initializing the view components.
     */
    @SuppressLint("SetTextI18n")
    private fun initializeViewComponents(view: View) {
        try {
            itemCountTextView = view.findViewById(R.id.itemQuantityHeader)
            itemCountTextView.text = "Basket size: ${basketRepository.getBasketSize(customerRepository.getCustomer()?.getAccountId())}"
            totalPriceTextView = view.findViewById(R.id.priceText)
            totalPriceTextView.text = currencyFormat.format(basketRepository.getBasketTotalPrice(customerRepository.getCustomer()?.getAccountId()))
            checkoutButton = view.findViewById(R.id.checkoutButton)
        } catch (e: Exception) {
            notification.sendNotification("Error while loading basket page.", requireActivity() as AppCompatActivity)
            Log.d("BasketView", "Error initializing view components: $e")
        }
    }
}