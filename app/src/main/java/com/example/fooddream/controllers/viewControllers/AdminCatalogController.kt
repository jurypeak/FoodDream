package com.example.fooddream.controllers.viewControllers

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.adapters.AdminCatalogAdapter
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Product
import com.example.fooddream.views.AddProductView
import com.example.fooddream.views.AdminCatalogView
import com.example.fooddream.views.AdminSearchCatalogView
import com.example.fooddream.views.EditProductView
import com.example.fooddream.views.ProductView
import com.example.fooddream.views.ThreeDotsView

/**
 * AdminCatalogController is responsible for managing the admin catalog view in the application.
 * It handles user interactions, product management, and navigation within the admin catalog.
 *
 * @property productController Controller for managing product-related actions.
 * @property navigationController Controller for managing navigation actions.
 * @property notification Notification manager for displaying messages to the user.
 * @property productAdapter Adapter for displaying products in the catalog.
 */
class AdminCatalogController(
    private val productController: ProductController,
    private val navigationController: NavigationController,
    private val notification: Notification,
    private val productAdapter: AdminCatalogAdapter,
    private val productList: ArrayList<Product>
) {

    /**
     * Handles the click event on a product in the catalog.
     *
     * @param product The product that was clicked.
     */
    fun onProductClick(product: Product) {
        Log.d("Product Clicked", "Product ID: ${product.getProductId()}")
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
     */
    fun onDeleteProductClick(product: Product, view: Activity) {
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

    /**
     * Initializes the AdminCatalogController with the provided parameters.
     *
     * @param productController The controller for managing product-related actions.
     * @param navigationController The controller for managing navigation actions.
     * @param notification The notification manager for displaying messages to the user.
     * @param productAdapter The adapter for displaying products in the catalog.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addDataToList(
        view: Activity,
    ) {
        try {
            productController.getProductsInDB(
                Volley.newRequestQueue(view),
                BuildConfig.URL_PRODUCTS,
                null
            ) { products ->
                if (products != null) {
                    productList.clear()
                    productList.addAll(products)
                    productAdapter.notifyDataSetChanged()
                } else {
                    Log.e("Product Display Error", "Failed to display products.")
                }
            }
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while loading the catalog.", view)
            Log.d("Catalog Initialization Error", "$error")
        }
    }

    /**
     * Sets up click listeners for the navigation buttons in the admin catalog view.
     *
     * @param view The activity context for initializing the controllers.
     * @param productList The list of products to be displayed in the catalog.
     * @param homeButton The home button for navigating to the admin catalog view.
     * @param searchButton The search button for navigating to the admin search catalog view.
     * @param plusButton The plus button for navigating to the add product view.
     * @param threeDotsButton The three dots button for navigating to the miscellaneous menu.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setupClickListeners(
        view: Activity,
        homeButton: ImageView,
        searchButton: ImageView,
        plusButton: ImageView,
        threeDotsButton: ImageView,
    ) {
        try {
            homeButton.setOnClickListener {
                homeButton.setImageResource(R.drawable.house_red)
                searchButton.setImageResource(R.drawable.search)
                plusButton.setImageResource(R.drawable.plus)
                threeDotsButton.setImageResource(R.drawable.dots)
                navigationController.navigateToActivity(AdminCatalogView::class.java)
            }
            searchButton.setOnClickListener {
                searchButton.setImageResource(R.drawable.search_red)
                plusButton.setImageResource(R.drawable.plus)
                homeButton.setImageResource(R.drawable.house)
                threeDotsButton.setImageResource(R.drawable.dots)
                productList.clear()
                productAdapter.notifyDataSetChanged()
                navigationController.navigateToFragment(
                    AdminSearchCatalogView(),
                    R.id.fragment_container
                )
            }
            plusButton.setOnClickListener {
                plusButton.setImageResource(R.drawable.plus_red)
                searchButton.setImageResource(R.drawable.search)
                homeButton.setImageResource(R.drawable.house)
                threeDotsButton.setImageResource(R.drawable.dots)
                productList.clear()
                productAdapter.notifyDataSetChanged()
                navigationController.navigateToFragment(
                    AddProductView(),
                    R.id.fragment_container
                )
            }
            threeDotsButton.setOnClickListener {
                threeDotsButton.setImageResource(R.drawable.dots_red)
                searchButton.setImageResource(R.drawable.search)
                plusButton.setImageResource(R.drawable.plus)
                homeButton.setImageResource(R.drawable.house)
                productList.clear()
                navigationController.navigateToFragment(
                    ThreeDotsView(),
                    R.id.fragment_container
                )
            }
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading the catalog.", view)
            Log.d("Catalog Initialization Error", "$e")
        }
    }
}