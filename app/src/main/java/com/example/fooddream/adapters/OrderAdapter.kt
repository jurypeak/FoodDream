package com.example.fooddream.adapters

import ProductRepository
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddream.R
import com.example.fooddream.models.OrderItem
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

class OrderAdapter(
    private val view: AppCompatActivity,
    private val orderItemList: ArrayList<OrderItem>,
    private val onProductClick: (OrderItem) -> Unit,
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    // https://youtu.be/-hWVtzMTABQ

    private val productRepository = ProductRepository(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card_item, parent, false)
        return OrderViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderItem = orderItemList[position]
        val product = productRepository.getProduct(orderItem.getProductId())

        Picasso.get()
            .load(product?.getImageUrl())
            .into(holder.productImageView)

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK)
        holder.productNameTextView.text = orderItem.getItemName()
        holder.productQuantityTextView.text = orderItem.getQuantity().toString()
        holder.productPriceTextView.text = currencyFormat.format(orderItem.getPrice())

        holder.itemView.setOnClickListener {
            onProductClick(orderItem)
        }
    }

    override fun getItemCount(): Int {
        return orderItemList.size
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageView: ImageView = itemView.findViewById(R.id.order_item_image)
        val productNameTextView: TextView = itemView.findViewById(R.id.order_item_name)
        val productQuantityTextView: TextView = itemView.findViewById(R.id.order_item_quantity)
        val productPriceTextView: TextView = itemView.findViewById(R.id.order_item_price)
    }
}
