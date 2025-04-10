package com.example.fooddream.views

import CustomerRepository
import android.annotation.SuppressLint
import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddream.R
import com.example.fooddream.adapters.OrderHistoryAdapter
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.models.Order
import com.example.fooddream.models.Payment
import com.example.fooddream.repositories.AddressRepository
import com.example.fooddream.repositories.OrderRepository
import com.example.fooddream.repositories.PaymentRepository

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

        init(view)

    }

    private fun init(view: View) {
        sessionController = SessionController(requireActivity() as AppCompatActivity)
        customerRepository = CustomerRepository(requireActivity() as AppCompatActivity)
        orderRepository = OrderRepository(requireActivity() as AppCompatActivity)
        paymentRepository = PaymentRepository(requireActivity() as AppCompatActivity)

        recyclerView = view.findViewById(R.id.order_history_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(requireActivity() as AppCompatActivity, 1)

        orderHistoryList = ArrayList()
        paymentList = ArrayList()
        orderHistoryAdapter = OrderHistoryAdapter(
            requireActivity() as AppCompatActivity,
            orderHistoryList,
        ) { order ->
            val bundle = Bundle().apply {
                putInt("orderId", order.getOrderId())
            }
            val orderViewFragment = OrderView().apply {
                arguments = bundle
            }
            navigationController.navigateToFragment(orderViewFragment, R.id.fragment_container)
        }
        recyclerView.adapter = orderHistoryAdapter

        initializeViewComponents(view)
        addDataToList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addDataToList() {
        orderHistoryList.clear()
        orderHistoryList.addAll(orderRepository.getAllOrders())

        paymentList.clear()
        paymentList.addAll(paymentRepository.getPayments())

        orderHistoryAdapter.notifyDataSetChanged()
    }

    private fun initializeViewComponents(view: View) {
    }
}