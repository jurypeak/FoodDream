package com.example.fooddream.views

import CustomerRepository
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddream.R
import com.example.fooddream.adapters.OrderAdapter
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.viewControllers.OrderViewController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.OrderItem
import com.example.fooddream.repositories.AddressRepository
import com.example.fooddream.repositories.OrderItemRepository
import com.example.fooddream.repositories.OrderRepository
import com.example.fooddream.repositories.PaymentRepository

/**
 * OrderView is a Fragment that displays the details of a specific order.
 * It allows the user to view the order items, customer information, and payment details.
 *
 * @property recyclerView The RecyclerView that displays the list of order items.
 * @property orderList The list of order items to be displayed in the RecyclerView.
 * @property orderAdapter The adapter for the RecyclerView.
 * @property navigationController The controller for managing navigation between views.
 * @property orderViewController The controller for managing the order view.
 * @property notification The notification manager for displaying messages to the user.
 * @property customerRepository The repository for managing customer data.
 * @property orderRepository The repository for managing order data.
 * @property orderItemRepository The repository for managing order item data.
 * @property paymentRepository The repository for managing payment data.
 * @property addressRepository The repository for managing address data.
 */
class OrderView : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderList: ArrayList<OrderItem>
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var navigationController: NavigationController
    private lateinit var orderViewController: OrderViewController
    private lateinit var notification: Notification
    private lateinit var customerRepository: CustomerRepository
    private lateinit var orderRepository: OrderRepository
    private lateinit var orderItemRepository: OrderItemRepository
    private lateinit var paymentRepository: PaymentRepository
    private lateinit var addressRepository: AddressRepository
    private lateinit var emailTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var postcodeTextView: TextView
    private lateinit var townTextView: TextView
    private lateinit var paymentMethodTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var totalTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.order_page, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    /**
     * Initializes the OrderView by setting up the controllers, view components,
     * and the RecyclerView for displaying order items.
     *
     * @param view The root view of the fragment.
     */
    private fun init(view: View) {
        initializeControllers()
        initializeViewComponents(view)
        initializeRecycler(view)
    }

    /**
     * Initializes the RecyclerView and its adapter.
     *
     * @param view The root view of the fragment.
     *
     * This method sets up the RecyclerView, its layout manager, and its adapter.
     * It also populates the order items into the RecyclerView using the OrderViewController.
     *
     * @throws Exception if an error occurs during initialization.
     */
    fun initializeRecycler(view: View) {
        try {
            val orderId = arguments?.getInt("orderId") ?: 0

            recyclerView = view.findViewById(R.id.order_view)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(requireActivity() as AppCompatActivity, 1)

            orderList = ArrayList()

            orderAdapter = OrderAdapter(
                requireActivity() as AppCompatActivity,
                orderList
            ) { order -> orderViewController.onProductClick(requireActivity() as AppCompatActivity, order) }
            recyclerView.adapter = orderAdapter

            orderViewController = OrderViewController(
                navigationController,
                notification,
            )

            orderViewController.addDataToList(
                requireActivity() as AppCompatActivity,
                orderId,
                orderList,
                orderItemRepository,
                orderAdapter,
            )
        } catch (e: Exception) {
            notification.sendNotification("Error while gathering order items in order view.", requireActivity() as AppCompatActivity)
            Log.e("OrderView", "Error initializing view components: ${e.message}")
        }
    }

    /**
     * Initializes the controllers used in the OrderView.
     *
     * @throws Exception if an error occurs while initializing the controllers.
     */
    fun initializeControllers() {
        try {
            orderRepository = OrderRepository(requireActivity() as AppCompatActivity)
            customerRepository = CustomerRepository(requireActivity() as AppCompatActivity)
            addressRepository = AddressRepository(requireActivity() as AppCompatActivity)
            paymentRepository = PaymentRepository(requireActivity() as AppCompatActivity)
            orderItemRepository = OrderItemRepository(requireActivity() as AppCompatActivity)
            navigationController = NavigationController(requireActivity() as AppCompatActivity)
            notification = Notification()
        } catch (e: Exception) {
            notification.sendNotification("Error while loading order view.", requireActivity() as AppCompatActivity)
            Log.e("OrderView", "Error initializing controllers: $e")
        }
    }

    /**
     * Initializes the view components of the OrderView.
     *
     * @param view The root view of the fragment.
     *
     * This method sets up the TextViews for displaying order details such as email, name,
     * address, postcode, town, payment method, date, and total amount.
     *
     * @throws Exception if an error occurs while initializing the view components.
     */
    @SuppressLint("SetTextI18n")
    private fun initializeViewComponents(view: View) {
        try {
            val orderId = arguments?.getInt("orderId") ?: 0

            emailTextView = view.findViewById(R.id.email_address_order)
            nameTextView = view.findViewById(R.id.name_order)
            addressTextView = view.findViewById(R.id.address_order)
            postcodeTextView = view.findViewById(R.id.postcode_order)
            townTextView = view.findViewById(R.id.town_order)
            paymentMethodTextView = view.findViewById(R.id.payment_method_order)
            dateTextView = view.findViewById(R.id.date_order)
            totalTextView = view.findViewById(R.id.total_order)

            val order = orderRepository.getOrder(orderId, customerRepository.getCustomer()?.getAccountId() ?: 0)
            val address = addressRepository.getAddress(orderId)
            val payment = paymentRepository.getPayment(orderId)

            Log.d("OrderView", "${order?.getEmail()}")

            emailTextView.text = order?.getEmail() ?: ""
            nameTextView.text = "${order?.getFName() ?: ""} ${order?.getLName() ?: ""}"
            addressTextView.text = address?.getStreet() ?: ""
            postcodeTextView.text = address?.getPostcode() ?: ""
            townTextView.text = address?.getTown() ?: ""
            paymentMethodTextView.text = payment?.getPaymentMethod() ?: ""
            dateTextView.text = payment?.getPaymentDate() ?: ""
            totalTextView.text = payment?.getAmount()?.let { "£$it" } ?: "£0.00"
        } catch (e: Exception) {
            notification.sendNotification("Error while gathering order details in order view.", requireActivity() as AppCompatActivity)
            Log.e("OrderView", "Error initializing view components: ${e.message}")
        }
    }
}
