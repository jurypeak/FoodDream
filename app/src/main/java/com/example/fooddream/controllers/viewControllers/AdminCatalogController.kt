package com.example.fooddream.controllers.viewControllers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
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
 * @property context The context of the activity.
 * @property productController Controller for managing product-related actions.
 * @property navigationController Controller for managing navigation actions.
 * @property notification Notification manager for displaying messages to the user.
 * @property productAdapter Adapter for displaying products in the catalog.
 */
class AdminCatalogController(
    private val context: Context,
    private val productController: ProductController,
    private val navigationController: NavigationController,
    private val notification: Notification,
    private val productAdapter: AdminCatalogAdapter
) {

    /**
     * Initializes the controller with the provided parameters.
     *
     * @param context The context of the activity.
     * @param productController Controller for managing product-related actions.
     * @param navigationController Controller for managing navigation actions.
     * @param notification Notification manager for displaying messages to the user.
     * @param productAdapter Adapter for displaying products in the catalog.
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
     */
    fun onDeleteProductClick(product: Product) {
        notification.sendDeleteProductPrompt(context as AppCompatActivity) { confirmed ->
            if (confirmed) {
                productController.removeProduct(
                    product.getProductId(),
                    BuildConfig.URL_DELETE_PRODUCT,
                    Volley.newRequestQueue(context),
                    notification,
                    navigationController
                )
            }
        }
    }

    /**
     * Handles the click event for adding a product to the basket.
     *
     * @param product The product to be added to the basket.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addDataToList(
        productList: ArrayList<Product>,
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
        productList: ArrayList<Product>,
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