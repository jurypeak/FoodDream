package com.example.fooddream.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddream.R
import com.example.fooddream.models.Product
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        val product = productList[position]
        var currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK);
        Picasso.get()
            .load(product.getImageUrl())
            .into(holder.imageView)
        holder.nameTextView.text = (product.getProductName())
        holder.priceTextView.text = (currencyFormat.format(product.getProductPrice()))
        holder.stockTextView.text = (product.getProductStock().toString())
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.productImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        val stockTextView: TextView = itemView.findViewById(R.id.productStockTextView)
    }
}