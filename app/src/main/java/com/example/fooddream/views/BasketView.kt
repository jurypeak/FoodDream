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
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.BasketItem
import com.example.fooddream.repositories.BasketRepository
import java.util.Locale

class BasketView : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var basketList: ArrayList<BasketItem>
    private lateinit var basketAdapter: BasketAdapter
    private lateinit var navigationController: NavigationController
    private lateinit var sessionController: SessionController
    private lateinit var basketRepository: BasketRepository
    private lateinit var customerRepository: CustomerRepository
    private lateinit var notification: Notification
    private lateinit var itemCountTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var checkoutButton: Button

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

        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        customerRepository = CustomerRepository(requireActivity() as AppCompatActivity)
        basketRepository = BasketRepository(requireActivity() as AppCompatActivity)
        notification = Notification()

        init(view)

    }

    private fun init(view: View) {
        try {
            sessionController = SessionController(requireActivity() as AppCompatActivity)

            recyclerView = view.findViewById(R.id.basket_view)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(requireActivity() as AppCompatActivity, 1)

            basketList = ArrayList()
            basketAdapter = BasketAdapter(
                requireActivity() as AppCompatActivity,
                basketList,
                { basketItem ->
                    Log.d("RecyclerViewClick", "Clicked basketItem: ${basketItem.getItemName()} with ID: ${basketItem.getProductId()}")
                    val bundle = Bundle().apply {
                        putInt("ProductId", basketItem.getProductId())
                    }
                    val productViewFragment = ProductView().apply {
                        arguments = bundle
                    }
                    navigationController.navigateToFragment(productViewFragment, R.id.fragment_container)
                },
                { product ->
                    Log.d("AddToBasket", "Added product: ${product.getItemName()} to the basket")
                    updateHeaderInfo()
                },
                { product ->
                    Log.d("RemoveFromBasket", "Removed product: ${product.getItemName()} from the basket")
                    updateHeaderInfo()
                },
                { product ->
                    Log.d("IncrementQuantity", "Incremented product quantity: ${product.getItemName()} in the basket")
                    updateHeaderInfo()
                }
            )
            recyclerView.adapter = basketAdapter
        } catch (e: Exception) {
            notification.sendNotification("Error while gathering basket items in basket.", requireActivity() as AppCompatActivity)
            Log.d("BasketView", "Error initializing RecyclerView: $e")
        }

        initializeViewComponents(view)
        addDataToList()
        setListeners()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addDataToList() {
        try {
            basketList.addAll(basketRepository.getAllBasketItems(customerRepository.getCustomer()?.getAccountId()))
            basketAdapter.notifyDataSetChanged()
        } catch (e: Exception) {
            notification.sendNotification("Error while loading basket items.", requireActivity() as AppCompatActivity)
            Log.d("BasketView", "Error adding data to list: $e")
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateHeaderInfo() {
        try {
            itemCountTextView.text = "${basketRepository.getBasketSize(customerRepository.getCustomer()?.getAccountId())} Items"
            totalPriceTextView.text = currencyFormat.format(basketRepository.getBasketTotalPrice(customerRepository.getCustomer()?.getAccountId()))
        } catch (e: Exception) {
            notification.sendNotification("Error while getting number of items & total price of items in basket.", requireActivity() as AppCompatActivity)
            Log.d("BasketView", "Error updating header info: $e")
        }
    }

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

    private fun setListeners() {
        try {
            checkoutButton.setOnClickListener {
                if (basketRepository.getBasketSize(customerRepository.getCustomer()?.getAccountId()) == 0) {
                    notification.sendNotification("Basket is empty.", requireActivity() as AppCompatActivity)
                    notification.sendNotification("Please add items to your basket before proceeding to checkout.", requireActivity() as AppCompatActivity)
                    return@setOnClickListener
                }
                else {
                    navigationController.navigateToFragment(CheckoutView(), R.id.fragment_container)
                }
            }
        } catch (e: Exception) {
            notification.sendNotification("Error while loading in basket page.", requireActivity() as AppCompatActivity)
            Log.d("BasketView", "Error setting listeners: $e")
        }
    }
}