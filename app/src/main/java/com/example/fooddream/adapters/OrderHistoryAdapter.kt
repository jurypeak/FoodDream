package com.example.fooddream.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddream.R
import com.example.fooddream.models.Order
import com.example.fooddream.models.Payment
import com.example.fooddream.repositories.PaymentRepository
import java.text.NumberFormat
import java.util.Locale

// https://youtu.be/-hWVtzMTABQ

class OrderHistoryAdapter(
    private val view: AppCompatActivity,
    private val orderHistoryList: ArrayList<Order>,
    private val onProductClick: (Order) -> Unit,
):
    RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_history_card, parent, false)
        return OrderHistoryViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: OrderHistoryViewHolder,
        position: Int
    ) {
        val orderHistoryItem = orderHistoryList[position]
        val paymentItem = PaymentRepository(view).getPayments()
            .find { it.getOrderId() == orderHistoryItem.getOrderId() }
            ?: return
        var currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK)
        holder.orderNumberView.text = ("Order #${orderHistoryItem.getOrderId()}")
        holder.totalTextView.text = (currencyFormat.format(paymentItem.getAmount()))
        holder.dateTextView.text = (orderHistoryItem.getOrderDate())

        holder.itemView.setOnClickListener {
            onProductClick(orderHistoryItem)
        }
    }

    override fun getItemCount(): Int {
        return orderHistoryList.size
    }

    class OrderHistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val orderNumberView: TextView = itemView.findViewById(R.id.order_number)
        val totalTextView: TextView = itemView.findViewById(R.id.total)
        val dateTextView: TextView = itemView.findViewById(R.id.date)
    }
}