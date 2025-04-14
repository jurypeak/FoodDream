package com.example.fooddream.controllers.viewControllers

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.adapters.AdminCatalogAdapter
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Product
import com.example.fooddream.views.EditProductView
import com.example.fooddream.views.ProductView

/**
 * AdminSearchCatalogController is responsible for managing the admin search catalog view in the application.
 * It handles user interactions, product management, and navigation within the admin search catalog.
 *
 * @property productController Controller for managing product-related actions.
 * @property navigationController Controller for managing navigation actions.
 * @property notification Notification manager for displaying messages to the user.
 * @property productAdapter Adapter for displaying products in the catalog.
 * @property productList List of products to be displayed in the catalog.
 */
class AdminSearchCatalogController(
    private val productController: ProductController,
    private val navigationController: NavigationController,
    private val notification: Notification,
    private val productAdapter: AdminCatalogAdapter,
    private val productList: ArrayList<Product>
) {

    /**
     * Initializes the controller with the provided parameters.
     *
     * @param productController Controller for managing product-related actions.
     * @param navigationController Controller for managing navigation actions.
     * @param notification Notification manager for displaying messages to the user.
     * @param productAdapter Adapter for displaying products in the catalog.
     * @param productList List of products to be displayed in the catalog.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addDataToList(
        view: AppCompatActivity,
        searchQuery: String?
    ) {
        try {
            productController.getProductsInDB(
                Volley.newRequestQueue(view),
                BuildConfig.URL_PRODUCTS,
                searchQuery
            ) { products ->
                if (products != null) {
                    productList.clear()
                    productList.addAll(products)
                    productAdapter.notifyDataSetChanged()
                } else {
                    Log.e("Product Display Error", "Failed to display products.")
                }
            }
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading the catalog.", view)
            Log.d("Catalog Initialization Error", "$e")
        }
    }

    /**
     * Handles the click event on a product in the catalog.
     *
     * @param product The product that was clicked.
     */
    fun onProductClick(product: Product) {
        val bundle = Bundle().apply {
            putInt("ProductId", product.getProductId())
        }
        val productView = ProductView().apply {
            arguments = bundle
        }
        navigationController.navigateToFragment(productView, R.id.fragment_container)
    }

    /**
     * Handles the click event for editing a product.
     *
     * @param product The product to be edited.
     */
    fun onEditProductClick(product: Product) {
        val bundle = Bundle().apply {
            putInt("ProductId", product.getProductId())
        }
        val editView = EditProductView().apply {
            arguments = bundle
        }
        navigationController.navigateToFragment(editView, R.id.fragment_container)
    }

    /**
     * Handles the click event for deleting a product.
     *
     * @param product The product to be deleted.
     * @param view The activity context for displaying notifications.
     */
    fun onDeleteProductClick(product: Product, view: AppCompatActivity) {
        notification.sendDeleteProductPrompt(view) { confirmed ->
            if (confirmed) {
                productController.removeProduct(
                    product.getProductId(),
                    BuildConfig.URL_DELETE_PRODUCT,
                    Volley.newRequestQueue(view),
                    notification,
                    navigationController
                )
            }
        }
    }

    //https://youtu.be/ONWE38Tm2_8

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    /**
     * Sets up the click listeners for the search bar and handles text changes.
     * This method is responsible for filtering the product list based on the user's input.
     * It uses a delayed search to avoid excessive API calls while the user is typing.
     *
     * @param view The activity context for displaying notifications.
     * @param searchBar The EditText view for the search bar.
     *
     * @throws Exception if an error occurs while setting up the click listeners.
     */
    fun setupClickListeners(
        view: AppCompatActivity,
        searchBar: EditText
    ) {
        try {
            searchBar.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val query = s.toString().trim()

                    searchRunnable?.let { handler.removeCallbacks(it) }

                    searchRunnable = Runnable {
                        if (query.isNotEmpty()) {
                            Log.d("SearchBar", "Searching for: \"$query\"")
                            addDataToList(view, query)
                        } else {
                            Log.d("SearchBar", "Empty query, loading all products")
                            addDataToList(view, null)
                        }
                    }

                    handler.postDelayed(searchRunnable!!, 100)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        } catch (e: Exception) {
            notification.sendNotification("Error while loading search catalog page.", view)
            Log.e("AdminSearchCatalogView", "Error setting listeners", e)
        }
    }
}