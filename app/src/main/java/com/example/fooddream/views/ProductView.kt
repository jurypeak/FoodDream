package com.example.fooddream.views

import ProductRepository
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.controllers.AccountController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.models.Ingredient
import com.example.fooddream.models.Product
import com.example.fooddream.repositories.IngredientRepository
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

class ProductView : Fragment() {

    private lateinit var navigationController: NavigationController
    private lateinit var productRepository: ProductRepository
    private lateinit var ingredientRepository: IngredientRepository
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        productRepository = ProductRepository(requireActivity() as AppCompatActivity)
        ingredientRepository = IngredientRepository(requireActivity() as AppCompatActivity)

        initializeViewComponents(view)
        setListeners()

        var productId = arguments?.getInt("ProductId") ?: -1
        var currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK)
        Log.d("ProductView", "Received product ID: $productId")
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
    }

    private fun initializeViewComponents(view: View) {
        productImageView = view.findViewById(R.id.productImageView)
        productNameTextView = view.findViewById(R.id.productNameTextView)
        productDescriptionTextView = view.findViewById(R.id.productDescriptionTextView)
        productPriceTextView = view.findViewById(R.id.productPriceTextView)
        productStockTextView = view.findViewById(R.id.productStockTextView)
        productIngredientTextView = view.findViewById(R.id.productIngredientsTextView)
        productCOTextView = view.findViewById(R.id.productCOTextView)
    }
    private fun setListeners() {

    }
}