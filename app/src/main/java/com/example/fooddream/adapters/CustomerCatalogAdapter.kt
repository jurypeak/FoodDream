package com.example.fooddream.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddream.R
import com.example.fooddream.models.BasketItem
import com.example.fooddream.models.Product
import com.example.fooddream.repositories.BasketRepository
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

// https://youtu.be/-hWVtzMTABQ

class CustomerCatalogAdapter(
    private val view: AppCompatActivity,
    private val productList: ArrayList<Product>,
    private val onProductClick: (Product) -> Unit,
    private val onAddToBasketClick: (Product) -> Unit,
    private val onRemoveFromBasketClick: (Product) -> Unit,
    private val onIncrementItemQuantityClick: (Product) -> Unit
):
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
        val basketRepository = BasketRepository(view)
        val product = productList[position]
        var currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK)
        Picasso.get()
            .load(product.getImageUrl())
            .into(holder.imageView)
        holder.nameTextView.text = (product.getProductName())
        holder.priceTextView.text = (currencyFormat.format(product.getProductPrice()))
        holder.stockTextView.text = (product.getProductStock().toString())

        basketRepository.getBasketItem(product.getProductId())?.getQuantity()?.let {
            if (it >= product.getProductStock()) {
                holder.addTextView.isEnabled = false
                holder.addTextView.text = "Out of Stock"
                holder.addTextView.setTextColor(Color.RED)
            } else {
                holder.addTextView.isEnabled = true
                holder.addTextView.text = "Add to Basket"
                holder.addTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            }
        }

        basketRepository.getBasketItem(product.getProductId())?.getQuantity()?.let {
            if (it == 0) {
                holder.addTextView.text = "Add"
                holder.addTextView.isClickable = true
                holder.addImageView.visibility = View.GONE
                holder.minusImageView.visibility = View.GONE
            }
            else {
                holder.addImageView.visibility = View.VISIBLE
                holder.minusImageView.visibility = View.VISIBLE
                holder.addTextView.isClickable = false
                holder.addTextView.text = basketRepository.getBasketItem(
                    product.getProductId())?.getQuantity().toString()

                onAddToBasketClick(product)
            }
        }

        holder.itemView.setOnClickListener {
            if (it.id != R.id.addProductTextView) {
                onProductClick(product)
            }
        }

        holder.addTextView.setOnClickListener {
            var basketItem = BasketItem(
                product.getProductId(),
                1,
                1,
                product.getProductPrice(),
                product.getProductName()
            )
            basketRepository.saveBasketItem(
                basketItem
            )

            holder.addImageView.visibility = View.VISIBLE
            holder.minusImageView.visibility = View.VISIBLE
            holder.addTextView.isClickable = false
            holder.addTextView.text = basketItem.getQuantity().toString()

            onAddToBasketClick(product)
        }

        holder.minusImageView.setOnClickListener {
            basketRepository.getBasketItem(
                product.getProductId())?.getQuantity()?.let { it1 ->
                if (it1 > 1) {
                    basketRepository.decrementQuantity(product.getProductId())
                    holder.addTextView.text = basketRepository.getBasketItem(
                         product.getProductId())?.getQuantity().toString()
                    holder.addTextView.setTextColor(Color.BLACK)
                }
                if (it1 <= 1) {
                    holder.addTextView.text = "Add"
                    holder.addTextView.isClickable = true
                    holder.minusImageView.visibility = View.GONE
                    holder.addImageView.visibility = View.GONE
                    basketRepository.removeBasketItem(product.getProductId())
                    holder.addTextView.setTextColor(Color.BLACK)
                }
            }

            onRemoveFromBasketClick(product)
        }

        holder.addImageView.setOnClickListener {
            val basketItem = basketRepository.getBasketItem(product.getProductId())

            basketItem?.let { item ->
                val currentQuantity = item.getQuantity()
                val productStock = product.getProductStock()

                if (currentQuantity >= productStock) {
                    holder.addTextView.isEnabled = false
                    holder.addTextView.text = currentQuantity.toString()
                    holder.addTextView.setTextColor(Color.RED)
                } else if (currentQuantity > 0 && currentQuantity < productStock) {
                    basketRepository.incrementQuantity(product.getProductId())
                    holder.addTextView.text = basketRepository.getBasketItem(
                        product.getProductId())?.getQuantity().toString()
                    holder.addTextView.setTextColor(Color.BLACK)
                }
            }

            onIncrementItemQuantityClick(product)
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.productImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        val stockTextView: TextView = itemView.findViewById(R.id.productStockTextView)
        val addTextView: TextView = itemView.findViewById(R.id.addProductTextView)
        val addImageView: ImageView = itemView.findViewById(R.id.addProductImageView)
        val minusImageView: ImageView = itemView.findViewById(R.id.minusProductImageView)
    }
}