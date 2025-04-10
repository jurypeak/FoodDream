package com.example.fooddream.adapters

import CustomerRepository
import ProductRepository
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
import com.example.fooddream.repositories.BasketRepository
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

// https://youtu.be/-hWVtzMTABQ

class BasketAdapter(
    private val view: AppCompatActivity,
    private val basketList: ArrayList<BasketItem>,
    private val onProductClick: (BasketItem) -> Unit,
    private val onAddToBasketClick: (BasketItem) -> Unit,
    private val onRemoveFromBasketClick: (BasketItem) -> Unit,
    private val onIncrementItemQuantityClick: (BasketItem) -> Unit
):
    RecyclerView.Adapter<BasketAdapter.ProductViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.basket_card, parent, false)
        return ProductViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        val basketRepository = BasketRepository(view)
        val customerRepository = CustomerRepository(view)
        val basketItem = basketList[position]
        var currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK)
        Picasso.get()
            .load(ProductRepository(view).getProduct(
                basketItem.getProductId())!!.getImageUrl())
            .into(holder.imageView)
        holder.nameTextView.text = (basketItem.getItemName())
        holder.priceTextView.text = (currencyFormat.format(basketItem.getPrice()))
        holder.stockTextView.text = (ProductRepository(view).getProduct(basketItem.getProductId())!!.getProductStock().toString())

        basketRepository.getBasketItem(basketItem.getProductId(), customerRepository.getCustomer()?.getAccountId())?.getQuantity()?.let {
            if (it >= ProductRepository(view).getProduct(basketItem.getProductId())!!.getProductStock()) {
                holder.quantityTextView.isEnabled = false
                holder.quantityTextView.setTextColor(Color.RED)
            } else {
                holder.quantityTextView.isEnabled = true
                holder.quantityTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            }
        }

        basketRepository.getBasketItem(basketItem.getProductId(), customerRepository.getCustomer()?.getAccountId())?.getQuantity()?.let {
            if (it == 0) {
                basketRepository.removeBasketItem(basketItem.getProductId(), customerRepository.getCustomer()?.getAccountId())
                val removedItemPosition = holder.adapterPosition
                basketList.removeAt(removedItemPosition)
                notifyItemRemoved(removedItemPosition)
            }
            else {
                holder.addImageView.visibility = View.VISIBLE
                holder.minusImageView.visibility = View.VISIBLE
                holder.quantityTextView.isClickable = false
                holder.quantityTextView.text = basketRepository.getBasketItem(
                    basketItem.getProductId(), customerRepository.getCustomer()?.getAccountId())?.getQuantity().toString()

                onAddToBasketClick(basketItem)
            }
        }

        holder.itemView.setOnClickListener {
            if (it.id != R.id.addProductTextView) {
                onProductClick(basketItem)
            }
        }

        holder.minusImageView.setOnClickListener {
            basketRepository.getBasketItem(
                basketItem.getProductId(), customerRepository.getCustomer()?.getAccountId())?.getQuantity()?.let { it1 ->
                if (it1 > 1) {
                    basketRepository.decrementQuantity(basketItem.getProductId(), customerRepository.getCustomer()?.getAccountId())
                    holder.quantityTextView.text = basketRepository.getBasketItem(
                        basketItem.getProductId(), customerRepository.getCustomer()?.getAccountId())?.getQuantity().toString()
                    holder.quantityTextView.setTextColor(Color.BLACK)
                }
                if (it1 <= 1) {
                    basketRepository.removeBasketItem(basketItem.getProductId(), customerRepository.getCustomer()?.getAccountId())
                    val removedItemPosition = holder.adapterPosition
                    basketList.removeAt(removedItemPosition)
                    notifyItemRemoved(removedItemPosition)
                }
            }

            onRemoveFromBasketClick(basketItem)
        }

        holder.addImageView.setOnClickListener {
            val basketItem = basketRepository.getBasketItem(basketItem.getProductId(), customerRepository.getCustomer()?.getAccountId())

            basketItem?.let { item ->
                val currentQuantity = item.getQuantity()
                val productStock = ProductRepository(view).getProduct(
                    basketItem.getProductId())!!.getProductStock()

                if (currentQuantity >= productStock) {
                    holder.quantityTextView.isEnabled = false
                    holder.quantityTextView.text = currentQuantity.toString()
                    holder.quantityTextView.setTextColor(Color.RED)
                } else if (currentQuantity > 0 && currentQuantity < productStock) {
                    basketRepository.incrementQuantity(basketItem.getProductId(), customerRepository.getCustomer()?.getAccountId())
                    holder.quantityTextView.text = basketRepository.getBasketItem(
                        basketItem.getProductId(), customerRepository.getCustomer()?.getAccountId())?.getQuantity().toString()
                    holder.quantityTextView.setTextColor(Color.BLACK)
                }
            }

            onIncrementItemQuantityClick(basketItem!!)
        }

    }

    override fun getItemCount(): Int {
        return basketList.size
    }

    class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.productImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        val stockTextView: TextView = itemView.findViewById(R.id.productStockTextView)
        val quantityTextView: TextView = itemView.findViewById(R.id.basketItemQuantityText)
        val addImageView: ImageView = itemView.findViewById(R.id.addIcon)
        val minusImageView: ImageView = itemView.findViewById(R.id.minusIcon)
    }
}