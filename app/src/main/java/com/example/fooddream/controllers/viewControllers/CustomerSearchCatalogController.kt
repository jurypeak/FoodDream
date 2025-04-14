package com.example.fooddream.controllers.viewControllers

import android.annotation.SuppressLint
import android.content.Context
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
import com.example.fooddream.adapters.CustomerCatalogAdapter
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Product
import com.example.fooddream.views.ProductView

/**
 * CustomerSearchCatalogController is responsible for managing the customer search catalog view in the application.
 * It handles user interactions, product management, and navigation within the customer search catalog.
 *
 * @property notification Notification manager for displaying messages to the user.
 * @property productController Controller for managing product-related actions.
 * @property navigationController Controller for managing navigation actions.
 */
class CustomerSearchCatalogController(
    private val notification: Notification,
    private val productController: ProductController,
    private val navigationController: NavigationController,
) {

    /**
     * Initializes the controller with the provided parameters.
     *
     * @param context The context of the activity.
     * @param view The activity where the buttons are located.
     * @param productAdapter Adapter for displaying products in the catalog.
     * @param productList List of products to be displayed in the catalog.
     * @param searchQuery The search query entered by the user.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addDataToList(
        context: Context,
        view: AppCompatActivity,
        productAdapter: CustomerCatalogAdapter,
        productList: ArrayList<Product>,
        searchQuery: String?
    ) {
        try {
            productController.getProductsInDB(
                Volley.newRequestQueue(context),
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
            notification.sendNotification("Error while loading search catalog page.", view)
            Log.d("CustomerSearchCatalogView", "Error initializing RecyclerView: $e")
        }
    }

    /**
     * Sets up click listeners for the product items in the catalog.
     *
     * @param product The product item that was clicked.
     */
    fun onProductClick(product: Product) {
        Log.d(
            "RecyclerViewClick",
            "Clicked product: ${product.getProductName()} with ID: ${product.getProductId()}"
        )
        val bundle = Bundle().apply {
            putInt("ProductId", product.getProductId())
        }
        val productViewFragment = ProductView().apply {
            arguments = bundle
        }
        navigationController.navigateToFragment(
            productViewFragment,
            R.id.fragment_container
        )
    }

    /**
     * Handles the click event for adding a product to the basket.
     *
     * @param product The product to be added to the basket.
     */
    fun onAddToBasket(product: Product) {

        Log.d("AddToBasket", "Added product: ${product.getProductName()} to the basket")
    }

    /**
     * Handles the click event for removing a product from the basket.
     *
     * @param product The product to be removed from the basket.
     */
    fun onRemoveFromBasket(product: Product) {
        Log.d("RemoveFromBasket", "Removed product: ${product.getProductName()} from the basket")
    }

    /**
     * Handles the click event for incrementing the quantity of a product in the basket.
     *
     * @param product The product whose quantity is to be incremented.
     */
    fun onIncrementQuantity(product: Product) {
        Log.d("IncrementQuantity", "Incremented product quantity: ${product.getProductName()} in the basket")
    }

    //https://youtu.be/ONWE38Tm2_8

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    /**
     * Sets up click listeners for the search bar and product adapter.
     *
     * @param searchBar The EditText view for the search bar.
     * @param productAdapter The adapter for displaying products in the catalog.
     * @param productList The list of products to be displayed in the catalog.
     * @param context The context of the activity.
     * @param view The activity where the buttons are located.
     */
    fun setupClickListeners(
        searchBar: EditText,
        productAdapter: CustomerCatalogAdapter,
        productList: ArrayList<Product>,
        context: Context,
        view: AppCompatActivity,
    ) {
        try {
            searchBar.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val query = s.toString().trim()

                    searchRunnable?.let { handler.removeCallbacks(it) }

                    searchRunnable = Runnable {
                        if (query.isNotEmpty()) {
                            Log.d("SearchBar", "Searching for: \"$query\"")
                            addDataToList(
                                context,
                                view,
                                productAdapter,
                                productList,
                                query
                            )
                        } else {
                            Log.d("SearchBar", "Empty query, loading all products")
                            addDataToList(
                                context,
                                view,
                                productAdapter,
                                productList,
                                null
                            )
                        }
                    }

                    handler.postDelayed(searchRunnable!!, 100)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        } catch (e: Exception) {
            notification.sendNotification("Error while loading search catalog page.", view)
            Log.e("CustomerSearchCatalogView", "Error setting listeners", e)
        }
    }
}