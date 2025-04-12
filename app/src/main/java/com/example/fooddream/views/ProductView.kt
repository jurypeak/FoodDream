package com.example.fooddream.views

import ProductRepository
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.R
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.repositories.IngredientRepository
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

class ProductView : Fragment() {

    private lateinit var navigationController: NavigationController
    private lateinit var productRepository: ProductRepository
    private lateinit var ingredientRepository: IngredientRepository
    private lateinit var notification: Notification
    private lateinit var productImageView: ImageView
    private lateinit var productNameTextView: TextView
    private lateinit var productDescriptionTextView: TextView
    private lateinit var productPriceTextView: TextView
    private lateinit var productStockTextView: TextView
    private lateinit var productIngredientTextView: TextView
    private lateinit var productCOTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.product_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        productRepository = ProductRepository(requireActivity() as AppCompatActivity)
        ingredientRepository = IngredientRepository(requireActivity() as AppCompatActivity)
        notification = Notification()

        init(view)
    }

    @SuppressLint("SetTextI18n")
    private fun init(view: View) {
        initializeViewComponents(view)
        setListeners()

        try {
            var productId = arguments?.getInt("ProductId") ?: -1

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
            notification.sendNotification("Error while loading product view.", requireActivity() as AppCompatActivity)
            Log.e("ProductView", "Error initializing product view: ${e.message}")
        }
    }

    private fun initializeViewComponents(view: View) {
        try {
            productImageView = view.findViewById(R.id.productImageView)
            productNameTextView = view.findViewById(R.id.productNameTextView)
            productDescriptionTextView = view.findViewById(R.id.productDescriptionTextView)
            productPriceTextView = view.findViewById(R.id.productPriceTextView)
            productStockTextView = view.findViewById(R.id.productStockTextView)
            productIngredientTextView = view.findViewById(R.id.productIngredientsTextView)
            productCOTextView = view.findViewById(R.id.productCOTextView)
        } catch (e: Exception) {
            notification.sendNotification("Error while initializing product view components.", requireActivity() as AppCompatActivity)
            Log.e("ProductView", "Error initializing view components: ${e.message}")
        }
    }
    private fun setListeners() {

    }
}