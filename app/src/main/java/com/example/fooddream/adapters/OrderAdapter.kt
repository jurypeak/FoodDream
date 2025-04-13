package com.example.fooddream.adapters

import com.example.fooddream.repositories.ProductRepository
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

/**
 * OrderAdapter is a RecyclerView adapter for displaying order items.
 * It binds the order data to the views in the RecyclerView.
 *
 * @param view The activity context used for inflating views.
 * @param orderItemList The list of order items to be displayed in the RecyclerView.
 * @param onProductClick A lambda function to handle click events on each order item.
 */
class OrderAdapter(
    private val view: AppCompatActivity,
    private val orderItemList: ArrayList<OrderItem>,
    private val onProductClick: (OrderItem) -> Unit,
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    // https://youtu.be/-hWVtzMTABQ

    /**
     * ProductRepository instance to retrieve product details.
     * This instance is used to load product images and details for each order item.
     */
    private val productRepository = ProductRepository(view)

    /**
     * Creates a new ViewHolder for the RecyclerView.
     * This method inflates the layout for each item in the RecyclerView.
     *
     * @param parent The parent ViewGroup that this ViewHolder will be attached to.
     * @param viewType The view type of the new View.
     * @return A new instance of OrderViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card_item, parent, false)
        return OrderViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderItem = orderItemList[position]
        val product = productRepository.getProduct(orderItem.getProductId())

        // Load product image using Picasso
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

    /**
     * Returns the total number of items in the order item list.
     *
     * @return The size of the order item list.
     */
    override fun getItemCount(): Int {
        return orderItemList.size
    }

    /**
     * ViewHolder class for holding the views of each order item in the RecyclerView.
     *
     * @param itemView The view for each order item.
     */
    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageView: ImageView = itemView.findViewById(R.id.order_item_image)
        val productNameTextView: TextView = itemView.findViewById(R.id.order_item_name)
        val productQuantityTextView: TextView = itemView.findViewById(R.id.order_item_quantity)
        val productPriceTextView: TextView = itemView.findViewById(R.id.order_item_price)
    }
}
