package com.example.fooddream.adapters

import CustomerRepository
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

/**
 * CustomerCatalogAdapter is a RecyclerView adapter for displaying product items in the customer catalog.
 * It binds the product data to the views in the RecyclerView and handles click events.
 *
 * @param view The activity context used for inflating views.
 * @param productList The list of products to be displayed in the RecyclerView.
 * @param onProductClick A lambda function to handle click events on each product item.
 * @param onAddToBasketClick A lambda function to handle click events for adding products to the basket.
 * @param onRemoveFromBasketClick A lambda function to handle click events for removing products from the basket.
 * @param onIncrementItemQuantityClick A lambda function to handle click events for incrementing item quantity in the basket.
 */
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
        val customerRepository = CustomerRepository(view)
        val product = productList[position]
        var currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK)
        Picasso.get()
            .load(product.getImageUrl())
            .into(holder.imageView)
        holder.nameTextView.text = (product.getProductName())
        holder.priceTextView.text = (currencyFormat.format(product.getProductPrice()))
        holder.stockTextView.text = (product.getProductStock().toString())

        /**
         * Check if the product is out of stock and update the UI accordingly.
         * If the product is out of stock, disable the add button and change its text color to red.
         */
        basketRepository.getBasketItem(product.getProductId(), customerRepository.getCustomer()?.getAccountId())?.getQuantity()?.let {
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

        /**
         * Check if the product is already in the basket and update the UI accordingly.
         * If the product is in the basket, show the quantity and hide the add button.
         */
        basketRepository.getBasketItem(product.getProductId(), customerRepository.getCustomer()?.getAccountId())?.getQuantity()?.let {
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
                    product.getProductId(), customerRepository.getCustomer()?.getAccountId())?.getQuantity().toString()

                onAddToBasketClick(product)
            }
        }

        /**
         * Set click listeners for the product item.
         * The product item click will trigger the onProductClick callback.
         */
        holder.itemView.setOnClickListener {
            if (it.id != R.id.addProductTextView) {
                onProductClick(product)
            }
        }

        /**
         * Set click listeners for the add and minus buttons.
         * The add button will increment the quantity of the product in the basket.
         * The minus button will decrement the quantity of the product in the basket.
         */
        holder.addTextView.setOnClickListener {
            var basketItem = BasketItem(
                product.getProductId(),
                1,
                1,
                product.getProductPrice(),
                product.getProductName()
            )
            basketRepository.saveBasketItem(
                basketItem,
                customerRepository.getCustomer()?.getAccountId(),
                product.getProductId()
            )

            holder.addImageView.visibility = View.VISIBLE
            holder.minusImageView.visibility = View.VISIBLE
            holder.addTextView.isClickable = false
            holder.addTextView.text = basketItem.getQuantity().toString()

            onAddToBasketClick(product)
        }

        holder.minusImageView.setOnClickListener {
            basketRepository.getBasketItem(
                product.getProductId(), customerRepository.getCustomer()?.getAccountId())?.getQuantity()?.let { it1 ->
                if (it1 > 1) {
                    basketRepository.decrementQuantity(product.getProductId(), customerRepository.getCustomer()?.getAccountId())
                    holder.addTextView.text = basketRepository.getBasketItem(
                         product.getProductId(), customerRepository.getCustomer()?.getAccountId())?.getQuantity().toString()
                    holder.addTextView.setTextColor(Color.BLACK)
                }
                if (it1 <= 1) {
                    holder.addTextView.text = "Add"
                    holder.addTextView.isClickable = true
                    holder.minusImageView.visibility = View.GONE
                    holder.addImageView.visibility = View.GONE
                    basketRepository.removeBasketItem(product.getProductId(), customerRepository.getCustomer()?.getAccountId())
                    holder.addTextView.setTextColor(Color.BLACK)
                }
            }

            onRemoveFromBasketClick(product)
        }

        /**
         * Set click listener for the add image button.
         * The add image button will increment the quantity of the product in the basket.
         */
        holder.addImageView.setOnClickListener {
            val basketItem = basketRepository.getBasketItem(product.getProductId(), customerRepository.getCustomer()?.getAccountId())

            basketItem?.let { item ->
                val currentQuantity = item.getQuantity()
                val productStock = product.getProductStock()

                if (currentQuantity >= productStock) {
                    holder.addTextView.isEnabled = false
                    holder.addTextView.text = currentQuantity.toString()
                    holder.addTextView.setTextColor(Color.RED)
                } else if (currentQuantity > 0 && currentQuantity < productStock) {
                    basketRepository.incrementQuantity(product.getProductId(), customerRepository.getCustomer()?.getAccountId())
                    holder.addTextView.text = basketRepository.getBasketItem(
                        product.getProductId(), customerRepository.getCustomer()?.getAccountId())?.getQuantity().toString()
                    holder.addTextView.setTextColor(Color.BLACK)
                }
            }

            onIncrementItemQuantityClick(product)
        }

    }

    /**
     * Returns the total number of items in the product list.
     *
     * @return The size of the product list.
     */
    override fun getItemCount(): Int {
        return productList.size
    }

    /**
     * ViewHolder class for holding the views of each product item in the RecyclerView.
     * This class is responsible for binding the product data to the views.
     *
     * @param itemView The view for each product item.
     */
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