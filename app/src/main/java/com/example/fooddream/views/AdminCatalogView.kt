package com.example.fooddream.views

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddream.R
import com.example.fooddream.adapters.AdminCatalogAdapter
import com.example.fooddream.controllers.viewControllers.AdminCatalogController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Product

/**
 * AdminCatalogView is an activity that displays a catalog of products for the admin.
 * It allows the admin to view, edit, and delete products from the catalog.
 *
 * @property recyclerView The RecyclerView that displays the list of products.
 * @property productList The list of products to be displayed in the RecyclerView.
 * @property productAdapter The adapter for the RecyclerView.
 * @property sessionController The controller for managing user sessions.
 * @property productController The controller for managing product-related operations.
 * @property navigationController The controller for managing navigation between views.
 * @property adminCatalogController The controller for managing the admin catalog view.
 * @property notification The notification manager for displaying messages to the user.
 * @property homeButton The button for navigating to the home view.
 * @property searchButton The button for searching products.
 * @property plusButton The button for adding a new product.
 * @property threeDotsButton The button for displaying additional options.
 */
class AdminCatalogView : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: ArrayList<Product>
    private lateinit var productAdapter: AdminCatalogAdapter
    private lateinit var sessionController: SessionController
    private lateinit var productController: ProductController
    private lateinit var navigationController: NavigationController
    private lateinit var adminCatalogController: AdminCatalogController
    private lateinit var notification: Notification
    private lateinit var homeButton: ImageView
    private lateinit var searchButton: ImageView
    private lateinit var plusButton: ImageView
    private lateinit var threeDotsButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.admin_catalog_page)

        init()
    }

    /**
     * Initializes the AdminCatalogView by setting up the controllers, view components,
     * RecyclerView, and UI actions.
     *
     * This method is called in the onCreate method of the activity.
     */
    private fun init() {
        initializeControllers()
        initializeViewComponents()
        initializeRecycler()
        setupUIActions()
    }

    /**
     * Initializes the RecyclerView and its adapter.
     *
     * This method sets up the RecyclerView with a GridLayoutManager and initializes the product adapter.
     * It also sets up the AdminCatalogController to manage product data.
     *
     * @throws Exception if an error occurs while initializing the RecyclerView.
     */
    private fun initializeRecycler() {
        try {
            recyclerView = findViewById(R.id.admin_catalog_view)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(this, 2)

            productList = ArrayList()

            productAdapter = AdminCatalogAdapter(
                this,
                productList,
                { product -> adminCatalogController.onProductClick(product) },
                { product -> adminCatalogController.onDeleteProductClick(product) },
                { product -> adminCatalogController.onEditProductClick(product) }
            )
            recyclerView.adapter = productAdapter

            adminCatalogController = AdminCatalogController(
                this,
                productController,
                navigationController,
                notification,
                productAdapter
            )

            adminCatalogController.addDataToList(
                productList,
                this
            )
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while loading the catalog.", this)
            Log.d("Catalog Initialization Error", "$error")
        }
    }

    /**
     * Initializes the controllers used in the AdminCatalogView.
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
            notification.sendNotification("Error occurred while loading the catalog.", this)
            Log.e("Catalog Initialization Error", "Error initializing controllers: ${e.message}")
        }
    }

    /**
     * Sets up the UI actions for the buttons in the AdminCatalogView.
     *
     * This method sets up click listeners for the home button, search button,
     * plus button, and three dots button.
     *
     * @throws Exception if an error occurs while setting up the UI actions.
     */
    private fun setupUIActions() {
        try {
            adminCatalogController.setupClickListeners(
                this,
                productList,
                homeButton,
                searchButton,
                plusButton,
                threeDotsButton
            )
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading the catalog.", this)
            Log.e("Catalog Initialization Error", "Error setting up UI actions: ${e.message}")
        }
    }

    /**
     * Initializes the view components used in the AdminCatalogView.
     *
     * This method sets up the buttons for navigation and actions.
     *
     * @throws Exception if an error occurs while initializing the view components.
     */
    private fun initializeViewComponents() {
        try {
            homeButton = findViewById(R.id.home_button)
            searchButton = findViewById(R.id.search_button)
            plusButton = findViewById(R.id.plus_button)
            threeDotsButton = findViewById(R.id.dots_button)
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while loading the catalog.", this)
            Log.d("Catalog Initialization Error", "$error")
        }
    }

    /**
     * Clears the user session when the activity is paused or stopped.
     */
    override fun onPause() {
        super.onPause()
        sessionController.clearUserSession()
        Log.d("LoginView", "Session cleared on pause.")
    }

    /**
     * Clears the user session when the activity is stopped.
     */
    override fun onStop() {
        super.onStop()
        sessionController.clearUserSession()
        Log.d("LoginView", "Session cleared on stop.")
    }
}