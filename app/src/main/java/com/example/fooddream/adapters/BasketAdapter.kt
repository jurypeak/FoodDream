package com.example.fooddream.adapters

import CustomerRepository
import com.example.fooddream.repositories.ProductRepository
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

/**
 * BasketAdapter is a RecyclerView adapter for displaying basket items.
 * It binds the basket data to the views in the RecyclerView and handles click events.
 *
 * @param view The activity context used for inflating views.
 * @param basketList The list of basket items to be displayed in the RecyclerView.
 * @param onProductClick A lambda function to handle click events on each product item.
 * @param onAddToBasketClick A lambda function to handle click events for adding products to the basket.
 * @param onRemoveFromBasketClick A lambda function to handle click events for removing products from the basket.
 * @param onIncrementItemQuantityClick A lambda function to handle click events for incrementing item quantity in the basket.
 */
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

        /**
         * Check if the quantity of the basket item is greater than or equal to the product stock.
         * If it is, disable the quantity TextView and set its text color to red.
         * Otherwise, enable the quantity TextView and set its text color to black.
         */
        basketRepository.getBasketItem(basketItem.getProductId(), customerRepository.getCustomer()?.getAccountId())?.getQuantity()?.let {
            if (it >= ProductRepository(view).getProduct(basketItem.getProductId())!!.getProductStock()) {
                holder.quantityTextView.isEnabled = false
                holder.quantityTextView.setTextColor(Color.RED)
            } else {
                holder.quantityTextView.isEnabled = true
                holder.quantityTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            }
        }

        /**
         * Check if the quantity of the basket item is 0.
         * If it is, remove the basket item from the repository and update the RecyclerView.
         * Otherwise, set the visibility of the add and minus ImageViews to visible,
         * disable the quantity TextView, and set its text to the current quantity.
         */
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

        /**
         * Set click listeners for the product item.
         * The product item click will trigger the onProductClick callback.
         */
        holder.itemView.setOnClickListener {
            if (it.id != R.id.addProductTextView) {
                onProductClick(basketItem)
            }
        }

        /**
         * Set click listeners for the add and minus buttons.
         * The add button will increment the quantity of the basket item,
         * and the minus button will decrement the quantity or remove the item from the basket.
         */
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

        /**
         * Set click listener for the add button.
         * The add button will increment the quantity of the basket item.
         */
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

    /**
     * Returns the total number of items in the basket list.
     *
     * @return The size of the basket list.
     */
    override fun getItemCount(): Int {
        return basketList.size
    }

    /**
     * ViewHolder class for holding the views of each product item in the RecyclerView.
     *
     * @param itemView The view for each product item.
     */
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