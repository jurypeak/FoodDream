package com.example.fooddream.controllers.viewControllers

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.messengers.Notification
import com.example.fooddream.repositories.IngredientRepository
import com.example.fooddream.repositories.ProductRepository
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

/**
 * ProductViewController is responsible for managing the product view in the application.
 * It handles the initialization of the product screen and displays product details.
 */
class ProductViewController() {

    /**
     * Initializes the product screen with the provided product details.
     *
     * @param view The activity where the product details are displayed.
     * @param productId The ID of the product to be displayed.
     * @param productImageView The ImageView for displaying the product image.
     * @param productNameTextView The TextView for displaying the product name.
     * @param productDescriptionTextView The TextView for displaying the product description.
     * @param productPriceTextView The TextView for displaying the product price.
     * @param productStockTextView The TextView for displaying the product stock information.
     * @param productIngredientTextView The TextView for displaying the product ingredients.
     * @param productCOTextView The TextView for displaying the product CO information.
     * @param ingredientRepository The repository for managing ingredient data.
     * @param productRepository The repository for managing product data.
     * @param notification The notification manager for displaying messages to the user.
     *
     * @throws Exception if an error occurs while initializing the product screen.
     */
    @SuppressLint("SetTextI18n")
    fun initializeProductScreen(
        view: AppCompatActivity,
        productId: Int,
        productImageView: ImageView,
        productNameTextView: TextView,
        productDescriptionTextView: TextView,
        productPriceTextView: TextView,
        productStockTextView: TextView,
        productIngredientTextView: TextView,
        productCOTextView: TextView,
        ingredientRepository: IngredientRepository,
        productRepository: ProductRepository,
        notification: Notification
    ) {
        try {
            var currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK)

            val product = productRepository.getProduct(productId)
            val ingredients = ingredientRepository.getIngredients(productId)

            Picasso.get()
                .load(product?.getImageUrl())
                .into(productImageView)
            productNameTextView.text = product?.getProductName()
            productDescriptionTextView.text = product?.getProductDescription()
            productPriceTextView.text = currencyFormat.format(product?.getProductPrice())
            productStockTextView.text = "${product?.getProductStock()} in stock."
            productIngredientTextView.text = ingredients.joinToString(", ") { it.getIngredientName() }
            productCOTextView.text = product?.getProductCO()
        } catch (e: Exception) {
            notification.sendNotification("Error while loading product view.", view)
            Log.e("ProductView", "Error initializing product view: ${e.message}")
        }
    }
}