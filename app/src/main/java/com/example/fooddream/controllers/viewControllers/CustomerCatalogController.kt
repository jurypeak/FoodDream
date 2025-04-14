package com.example.fooddream.controllers.viewControllers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.adapters.CustomerCatalogAdapter
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Product
import com.example.fooddream.views.AccountView
import com.example.fooddream.views.BasketView
import com.example.fooddream.views.CustomerCatalogView
import com.example.fooddream.views.CustomerSearchCatalogView
import com.example.fooddream.views.ProductView
import com.example.fooddream.views.ThreeDotsView

/**
 * CustomerCatalogController is responsible for managing the customer catalog view in the application.
 * It handles user interactions, product management, and navigation within the customer catalog.
 *
 * @property navigationController Controller for managing navigation actions.
 * @property productController Controller for managing product-related actions.
 * @property notification Notification manager for displaying messages to the user.
 */
class CustomerCatalogController(
    private val navigationController: NavigationController,
    private val productController: ProductController,
    private val notification: Notification
) {

//    fun initializeCustomerCatalogScreen(
//        customerRepository: CustomerRepository,
//        orderManager: OrderManager,
//        view: Activity,
//    ) {
//        // https://medium.com/@rushabhprajapati20/mastering-kotlin-coroutines-in-android-8457a6e5dd12
//        // Not working
//        CoroutineScope(Dispatchers.Main).launch {
//            try {
//                val accountId = customerRepository.getCustomer()?.getAccountId()
//                if (accountId != null) {
//                    orderManager.getOrders(
//                        Volley.newRequestQueue(view),
//                        BuildConfig.URL_GET_ORDERS,
//                        accountId
//                    )
//                } else {
//                    Log.e("CustomerCatalogView", "Account ID is missing.")
//                }
//            } catch (e: Exception) {
//                notification.sendNotification("Error failed to fetch orders.", view)
//                Log.e("CustomerCatalogView", "Error fetching orders: $e")
//            }
//        }
//    }


    /**
     * Handles the click event on a product in the catalog.
     *
     * @param product The product that was clicked.
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

    /**
     * Gets the list of products from the database and updates the product list.
     *
     * @param context The context of the activity.
     * @param view The activity view.
     * @param productAdapter Adapter for displaying products in the catalog.
     * @param productList The list of products to be displayed.
     *
     * This method fetches the products from the database and updates the product list.
     * It also handles any exceptions that may occur during the process.
     *
     * @throws Exception If an error occurs while fetching products or updating the list.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addDataToList(
        context: Context,
        view: Activity,
        productAdapter: CustomerCatalogAdapter,
        productList: ArrayList<Product>,
    ) {
        try {
            productController.getProductsInDB(
                Volley.newRequestQueue(context),
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
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading the catalog page.", view)
            Log.d("Catalog Initialization Error", "$e")
        }
    }

    /**
     * Sets up click listeners for the navigation buttons in the customer catalog view.
     *
     * @param view The activity context for initializing the controllers.
     * @param productAdapter Adapter for displaying products in the catalog.
     * @param productList The list of products to be displayed in the catalog.
     * @param homeButton The home button for navigating to the customer catalog view.
     * @param searchButton The search button for navigating to the customer search catalog view.
     * @param basketButton The basket button for navigating to the basket view.
     * @param accountButton The account button for navigating to the account view.
     * @param threeDotsButton The three dots button for navigating to the miscellaneous menu.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setupClickListeners(
        view: Activity,
        productAdapter: CustomerCatalogAdapter,
        productList: ArrayList<Product>,
        homeButton: ImageView,
        searchButton: ImageView,
        basketButton: ImageView,
        accountButton: ImageView,
        threeDotsButton: ImageView
    ) {
        try {
            homeButton.setOnClickListener {
                homeButton.setImageResource(R.drawable.house_red)
                basketButton.setImageResource(R.drawable.basket)
                accountButton.setImageResource(R.drawable.user)
                searchButton.setImageResource(R.drawable.search)
                threeDotsButton.setImageResource(R.drawable.dots)
                navigationController.navigateToActivity(CustomerCatalogView::class.java)
            }
            searchButton.setOnClickListener {
                searchButton.setImageResource(R.drawable.search_red)
                basketButton.setImageResource(R.drawable.basket)
                accountButton.setImageResource(R.drawable.user)
                homeButton.setImageResource(R.drawable.house)
                threeDotsButton.setImageResource(R.drawable.dots)
                productList.clear()
                productAdapter.notifyDataSetChanged()
                navigationController.navigateToFragment(
                    CustomerSearchCatalogView(),
                    R.id.fragment_container
                )
            }
            basketButton.setOnClickListener {
                basketButton.setImageResource(R.drawable.basket_red)
                searchButton.setImageResource(R.drawable.search)
                accountButton.setImageResource(R.drawable.user)
                homeButton.setImageResource(R.drawable.house)
                threeDotsButton.setImageResource(R.drawable.dots)
                productList.clear()
                productAdapter.notifyDataSetChanged()
                navigationController.navigateToFragment(
                    BasketView(),
                    R.id.fragment_container
                )
            }
            accountButton.setOnClickListener {
                accountButton.setImageResource(R.drawable.user_red)
                searchButton.setImageResource(R.drawable.search)
                basketButton.setImageResource(R.drawable.basket)
                homeButton.setImageResource(R.drawable.house)
                threeDotsButton.setImageResource(R.drawable.dots)
                productList.clear()
                productAdapter.notifyDataSetChanged()
                navigationController.navigateToFragment(
                    AccountView(),
                    R.id.fragment_container
                )
            }
            threeDotsButton.setOnClickListener {
                threeDotsButton.setImageResource(R.drawable.dots_red)
                searchButton.setImageResource(R.drawable.search)
                basketButton.setImageResource(R.drawable.basket)
                accountButton.setImageResource(R.drawable.user)
                homeButton.setImageResource(R.drawable.house)
                productList.clear()
                navigationController.navigateToFragment(
                    ThreeDotsView(),
                    R.id.fragment_container
                )
            }
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading customer catalog page.", view)
            Log.d("Catalog Initialization Error", "$e")
        }
    }
}