package com.example.fooddream.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddream.R
import com.example.fooddream.models.Product

// https://youtu.be/-hWVtzMTABQ

class CustomerCatalogAdapter(private val productList: ArrayList<Product>):
    RecyclerView.Adapter<CustomerCatalogAdapter.ProductViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.catalog_card, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        val product = productList[position]
        holder.imageView.setImageResource(product.getImageURL())
        holder.nameTextView.text = (product.getProductName())
        holder.priceTextView.text = (product.getProductPrice().toString())
        holder.stockTextView.text = (product.getProductStock().toString())
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.productImageView)
        val nameTextView : TextView = itemView.findViewById(R.id.productNameTextView)
        val priceTextView : TextView = itemView.findViewById(R.id.productPriceTextView)
        val stockTextView : TextView = itemView.findViewById(R.id.productStockTextView)
    }
}