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
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.OrderItem
import com.example.fooddream.repositories.AddressRepository
import com.example.fooddream.repositories.OrderItemRepository
import com.example.fooddream.repositories.OrderRepository
import com.example.fooddream.repositories.PaymentRepository

class OrderView : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderList: ArrayList<OrderItem>
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var navigationController: NavigationController
    private lateinit var notification: Notification
    private lateinit var sessionController: SessionController
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

        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        notification = Notification()

        init(view)
    }

    private fun init(view: View) {
        try {
            sessionController = SessionController(requireActivity() as AppCompatActivity)
            customerRepository = CustomerRepository(requireActivity() as AppCompatActivity)
            orderRepository = OrderRepository(requireActivity() as AppCompatActivity)
            paymentRepository = PaymentRepository(requireActivity() as AppCompatActivity)
            orderItemRepository = OrderItemRepository(requireActivity() as AppCompatActivity)
            addressRepository = AddressRepository(requireActivity() as AppCompatActivity)

            recyclerView = view.findViewById(R.id.order_view)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(requireActivity() as AppCompatActivity, 1)

            orderList = ArrayList()
            orderAdapter = OrderAdapter(
                requireActivity() as AppCompatActivity,
                orderList
            ) { order ->
                val bundle = Bundle().apply {
                    putInt("ProductId", order.getProductId())
                }
                val productViewFragment = ProductView().apply {
                    arguments = bundle
                }
                navigationController.navigateToFragment(productViewFragment, R.id.fragment_container)
            }
            recyclerView.adapter = orderAdapter
        } catch (e: Exception) {
            notification.sendNotification("Error while gathering order items in order view.", requireActivity() as AppCompatActivity)
            Log.e("OrderView", "Error initializing view components: ${e.message}")
        }

        initializeViewComponents(view)
        addDataToList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addDataToList() {
        try {
            val allOrderItems = ArrayList<OrderItem>()

            val orderId = arguments?.getInt("orderId") ?: 0
            Log.d("OrderView", "Order ID: $orderId")
            val orderItem = orderItemRepository.getOrderItems(orderId)
            allOrderItems.addAll(orderItem)

            orderList.clear()
            orderList.addAll(allOrderItems)

            orderAdapter.notifyDataSetChanged()
        } catch (e: Exception) {
            notification.sendNotification("Error while gathering order items in order view.", requireActivity() as AppCompatActivity)
            Log.e("OrderView", "Error adding data to list: ${e.message}")
        }
    }

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
