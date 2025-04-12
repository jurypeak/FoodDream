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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.R
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.OrderController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.repositories.BasketRepository
import java.util.Locale

class CheckoutView : Fragment() {

    private lateinit var navigationController: NavigationController
    private lateinit var orderController: OrderController
    private lateinit var customerRepository: CustomerRepository
    private lateinit var notification: Notification
    private lateinit var basketRepository: BasketRepository
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var itemCountTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var emailView: EditText
    private lateinit var nameView: EditText
    private lateinit var addressView: EditText
    private lateinit var postcodeView: EditText
    private lateinit var townView: EditText
    private lateinit var paymentItem: String
    private lateinit var payButton: Button
    private lateinit var adapterItems: ArrayAdapter<String>

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK)
    private val paymentItems = listOf("Debit/Credit Card", "PayPal", "Google Pay")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.checkout_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        orderController = OrderController(requireActivity() as AppCompatActivity)
        basketRepository = BasketRepository(requireActivity() as AppCompatActivity)
        customerRepository = CustomerRepository(requireActivity() as AppCompatActivity)
        notification = Notification()

        init(view)

    }

    private fun init(view: View) {
        try {
            autoCompleteTextView = view.findViewById(R.id.auto_complete_text)
            adapterItems = ArrayAdapter<String>(requireContext(), R.layout.list_item, paymentItems)

            autoCompleteTextView.setAdapter(adapterItems)
        } catch (e: Exception) {
            notification.sendNotification("Error while loading checkout page.", requireActivity() as AppCompatActivity)
            Log.e("CheckoutView", "Error initializing AutoCompleteTextView: ${e.message}")
        }

        initializeViewComponents(view)
        setListeners()
    }

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

    private fun setListeners() {
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
                    requireActivity() as AppCompatActivity
                )
            }
        } catch (e: Exception) {
            notification.sendNotification("Error while loading checkout page.", requireActivity() as AppCompatActivity)
            Log.e("CheckoutView", "Error setting listeners: ${e.message}")
        }
    }
}