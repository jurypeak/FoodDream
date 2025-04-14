package com.example.fooddream.views

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddream.R
import com.example.fooddream.adapters.CustomerCatalogAdapter
import com.example.fooddream.controllers.viewControllers.CustomerCatalogController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Product

/**
 * CustomerCatalogView is an activity that displays a catalog of products for the customer.
 * It allows the customer to view product details and add products to their basket.
 *
 * @property recyclerView The RecyclerView that displays the list of products.
 * @property productList The list of products to be displayed in the RecyclerView.
 * @property productAdapter The adapter for the RecyclerView.
 * @property sessionController The controller for managing user sessions.
 * @property productController The controller for managing product-related operations.
 * @property navigationController The controller for managing navigation between views.
 * @property customerCatalogController The controller for managing the customer catalog view.
 * @property notification The notification manager for displaying messages to the user.
 * @property homeButton The button for navigating to the home view.
 * @property searchButton The button for searching products.
 * @property basketButton The button for viewing the basket.
 * @property accountButton The button for navigating to the account view.
 * @property threeDotsButton The button for displaying additional options.
 */
class CustomerCatalogView : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: ArrayList<Product>
    private lateinit var productAdapter: CustomerCatalogAdapter
    private lateinit var sessionController: SessionController
    private lateinit var productController: ProductController
    private lateinit var notification: Notification
    private lateinit var navigationController: NavigationController
    private lateinit var customerCatalogController: CustomerCatalogController
    private lateinit var homeButton: ImageView
    private lateinit var searchButton: ImageView
    private lateinit var basketButton: ImageView
    private lateinit var accountButton: ImageView
    private lateinit var threeDotsButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.customer_catalog_page)

        init()
    }

    /**
     * Initializes the CustomerCatalogView by setting up the controllers, view components,
     * and RecyclerView. It also sets up click listeners for UI actions.
     *
     * This method is called during the onCreate lifecycle method to set up the UI.
     */
    private fun init() {
        initializeControllers()
        initializeViewComponents()
        initializeRecycler()
        setUIActions()
    }

    /**
     * Initializes the controllers used in the CustomerCatalogView.
     *
     * This method creates instances of the SessionController, ProductController,
     * NavigationController, and Notification classes.
     *
     * @throws Exception if an error occurs while initializing the controllers.
     */
    private fun initializeControllers() {
        try {
            sessionController = SessionController(this)
            productController = ProductController(this)
            navigationController = NavigationController(this)
            notification = Notification()
        } catch (e: Exception) {
            notification.sendNotification("Error while loading customer catalog page.", this)
            Log.e("CustomerCatalogView", "Error initializing controllers: $e")
        }
    }

    /**
     * Initializes the RecyclerView and its adapter for displaying products.
     *
     * This method sets up the RecyclerView with a GridLayoutManager and initializes the product adapter.
     * It also creates an instance of the CustomerCatalogController to manage product-related actions.
     *
     * @throws Exception if an error occurs while initializing the RecyclerView.
     */
    private fun initializeRecycler() {
        try {
            recyclerView = findViewById(R.id.customer_catalog_view)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(this, 2)

            productList = ArrayList()

            productAdapter = CustomerCatalogAdapter(
                this,
                productList,
                { product -> customerCatalogController.onProductClick(product) },
                { product -> customerCatalogController.onAddToBasket(product) },
                { product -> customerCatalogController.onRemoveFromBasket(product) },
                { product -> customerCatalogController.onIncrementQuantity(product) }
            )
            recyclerView.adapter = productAdapter

            customerCatalogController = CustomerCatalogController(
                navigationController,
                productController,
                notification
            )

            customerCatalogController.addDataToList(
                this,
                this,
                productAdapter,
                productList
            )
        } catch (e: Exception) {
            notification.sendNotification("Error while loading customer catalog page.", this)
            Log.e("CustomerCatalogView", "Error initializing RecyclerView: $e")
        }
    }

    /**
     * Sets up click listeners for UI actions such as navigating to different views.
     *
     * This method is called after initializing the view components to ensure that
     * all UI elements are ready for interaction.
     *
     * @throws Exception if an error occurs while setting up UI actions.
     */
    private fun setUIActions() {
        try {
            customerCatalogController.setupClickListeners(
                this,
                productAdapter,
                productList,
                homeButton,
                searchButton,
                basketButton,
                accountButton,
                threeDotsButton
            )
        } catch (e: Exception) {
            notification.sendNotification("Error while setting UI actions.", this)
            Log.e("CustomerCatalogView", "Error setting UI actions: $e")
        }
    }

    /**
     * Initializes the view components such as buttons and other UI elements.
     *
     * This method is called during the onCreate lifecycle method to set up the UI.
     *
     * @throws Exception if an error occurs while initializing view components.
     */
    private fun initializeViewComponents() {
        try {
            homeButton = findViewById(R.id.home_button)
            searchButton = findViewById(R.id.search_button)
            basketButton = findViewById(R.id.basket_button)
            accountButton = findViewById(R.id.account_button)
            threeDotsButton = findViewById(R.id.dots_button)
        } catch (e: Exception) {
            notification.sendNotification("Error while loading customer catalog page.", this)
            Log.e("CustomerCatalogView", "Error initializing view components", e)
        }
    }
}
//    /**
//     * Clears the user session when the activity is paused or stopped.
//     */
//    override fun onPause() {
//        super.onPause()
//        sessionController.clearUserSession()
//        Log.d("LoginView", "Session cleared on pause.")
//    }
//
//    /**
//     * Clears the user session when the activity is stopped.
//     */
//    override fun onStop() {
//        super.onStop()
//        sessionController.clearUserSession()
//        Log.d("LoginView", "Session cleared on stop.")
//    }
//}