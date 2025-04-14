package com.example.fooddream.views

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddream.R
import com.example.fooddream.adapters.AdminCatalogAdapter
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.controllers.viewControllers.AdminSearchCatalogController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Product

/**
 * AdminSearchCatalogView is a Fragment that displays a searchable catalog of products for the admin.
 * It allows the admin to search for products, view product details, edit, and delete products.
 *
 * @property recyclerView The RecyclerView that displays the list of products.
 * @property productList The list of products to be displayed in the RecyclerView.
 * @property productAdapter The adapter for the RecyclerView.
 * @property navigationController The controller for managing navigation between views.
 * @property sessionController The controller for managing user sessions.
 * @property productController The controller for managing product-related operations.
 * @property adminSearchCatalogController The controller for managing the admin search catalog view.
 * @property notification The notification manager for displaying messages to the user.
 * @property searchBar The EditText for entering search queries.
 */
class AdminSearchCatalogView : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: ArrayList<Product>
    private lateinit var productAdapter: AdminCatalogAdapter
    private lateinit var navigationController: NavigationController
    private lateinit var sessionController: SessionController
    private lateinit var productController: ProductController
    private lateinit var adminSearchCatalogController: AdminSearchCatalogController
    private lateinit var notification: Notification
    private lateinit var searchBar: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.search_catalog_page, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view, requireActivity() as AppCompatActivity)
    }

    /**
     * Initializes the AdminSearchCatalogView by setting up the controllers, view components,
     * and the RecyclerView for displaying products.
     *
     * @param view The root view of the fragment.
     * @param context The activity context.
     *
     * This method initializes the controllers, view components, and RecyclerView.
     * It also sets up click listeners for UI actions.
     *
     * @throws Exception if an error occurs during initialization.
     */
    private fun init(view: View, context: AppCompatActivity) {
        initializeControllers(context)
        initializeViewComponents(view)
        initializeRecycler(view, context)
        setupUIActions(context)
    }

    /**
     * Initializes the RecyclerView and its adapter.
     *
     * @param view The root view of the fragment.
     * @param context The activity context.
     *
     * This method sets up the RecyclerView with a GridLayoutManager and initializes the product adapter.
     * It also populates the product list with data from the ProductController.
     *
     * @throws Exception if an error occurs during initialization.
     */
    private fun initializeRecycler(view: View, context: AppCompatActivity) {
        try {
            recyclerView = view.findViewById(R.id.search_catalog_view)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(context, 2)

            productList = ArrayList()

            productAdapter = AdminCatalogAdapter(
                context,
                productList,
                { product -> adminSearchCatalogController.onProductClick(product) },
                { product -> adminSearchCatalogController.onDeleteProductClick(product, context) },
                { product -> adminSearchCatalogController.onEditProductClick(product) }
            )

            adminSearchCatalogController = AdminSearchCatalogController(
                productController,
                navigationController,
                notification,
                productAdapter,
                productList
            )

            recyclerView.adapter = productAdapter

            adminSearchCatalogController.addDataToList(
                context,
                searchBar.text.toString()
            )
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while loading the catalog.", context)
            Log.d("Catalog Initialization Error", "$error")
        }
    }

    /**
     * Initializes the controllers used in the AdminSearchCatalogView.
     *
     * @param view The activity context.
     *
     * This method attempts to create instances of the SessionController,
     * ProductController, NavigationController, and Notification classes.
     * If an exception occurs, it sends a notification to the user and logs the error.
     *
     * @throws Exception if an error occurs while initializing the controllers.
     */
    private fun initializeControllers(view: AppCompatActivity) {
        try {
            sessionController = SessionController(view)
            productController = ProductController(view)
            navigationController = NavigationController(view)
            notification = Notification()
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading the catalog.", view)
            Log.e("Catalog Initialization Error", "Error initializing controllers: ${e.message}")
        }
    }

    /**
     * Sets up the UI actions for the AdminSearchCatalogView.
     *
     * @param view The activity context.
     *
     * This method sets up click listeners for the search bar and other UI elements.
     * It uses the AdminSearchCatalogController to handle the actions.
     *
     * @throws Exception if an error occurs while setting up UI actions.
     */
    private fun setupUIActions(view: AppCompatActivity) {
        try {
            adminSearchCatalogController.setupClickListeners(
                view,
                searchBar
            )
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading the catalog.", view)
            Log.e("Catalog Initialization Error", "Error setting up UI actions: ${e.message}")
        }
    }

    /**
     * Initializes the view components used in the AdminSearchCatalogView.
     *
     * @param view The root view of the fragment.
     *
     * This method attempts to find and initialize the search bar view component.
     * If an exception occurs, it sends a notification to the user and logs the error.
     *
     * @throws Exception if an error occurs while initializing the view components.
     */
    private fun initializeViewComponents(view: View) {
        try {
            searchBar = view.findViewById(R.id.search_bar)
        } catch (e: Exception) {
            notification.sendNotification("Error while loading search catalog page.", requireActivity() as AppCompatActivity)
            Log.e("AdminSearchCatalogView", "Error initializing view components", e)
        }
    }
}