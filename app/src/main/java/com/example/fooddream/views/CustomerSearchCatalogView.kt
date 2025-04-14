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
import com.example.fooddream.adapters.CustomerCatalogAdapter
import com.example.fooddream.controllers.viewControllers.CustomerSearchCatalogController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Product

/**
 * CustomerSearchCatalogView is a Fragment that displays a searchable catalog of products for customers.
 * It allows the customer to search for products and view product details.
 *
 * @property recyclerView The RecyclerView that displays the list of products.
 * @property productList The list of products to be displayed in the RecyclerView.
 * @property productAdapter The adapter for the RecyclerView.
 * @property navigationController The controller for managing navigation between views.
 * @property productController The controller for managing product-related operations.
 * @property customerSearchCatalogController The controller for managing the customer search catalog view.
 * @property notification The notification manager for displaying messages to the user.
 * @property searchBar The EditText for entering search queries.
 */
class CustomerSearchCatalogView : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: ArrayList<Product>
    private lateinit var productAdapter: CustomerCatalogAdapter
    private lateinit var navigationController: NavigationController
    private lateinit var productController: ProductController
    private lateinit var customerSearchCatalogController: CustomerSearchCatalogController
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

        init(view)
    }

    /**
     * Initializes the CustomerSearchCatalogView by setting up the controllers, view components,
     * and RecyclerView. It also sets up click listeners for UI actions.
     *
     * @param view The root view of the fragment.
     *
     * This method attempts to initialize the controllers, view components, and RecyclerView.
     * If an exception occurs, it sends a notification to the user and logs the error.
     *
     * @throws Exception if an error occurs while initializing the view.
     */
    private fun init(view: View) {
        initializeControllers()
        initializeViewComponents(view)
        initializeRecycler(view)
        setUIActions()
    }

    /**
     * Initializes the controllers used in the CustomerSearchCatalogView.
     *
     * This method attempts to create instances of the NavigationController,
     * ProductController, and Notification classes.
     * If an exception occurs, it sends a notification to the user and logs the error.
     *
     * @throws Exception if an error occurs while initializing the controllers.
     */
    private fun initializeControllers() {
        try {
            navigationController = NavigationController(requireActivity() as AppCompatActivity)
            productController = ProductController(requireActivity() as AppCompatActivity)
            notification = Notification()
        } catch (e: Exception) {
            notification.sendNotification("Error while loading search catalog page.", requireActivity() as AppCompatActivity)
            Log.e("CustomerSearchCatalogView", "Error initializing controllers: $e")
        }
    }

    /**
     * Initializes the RecyclerView and its adapter for displaying products.
     *
     * @param view The root view of the fragment.
     *
     * This method attempts to set up the RecyclerView and its adapter for displaying products.
     * If an exception occurs, it sends a notification to the user and logs the error.
     *
     * @throws Exception if an error occurs while initializing the RecyclerView.
     */
    private fun initializeRecycler(view: View) {
        try {
            recyclerView = view.findViewById(R.id.search_catalog_view)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(requireActivity() as AppCompatActivity, 2)

            productList = ArrayList()

            productAdapter = CustomerCatalogAdapter(
                requireActivity() as AppCompatActivity,
                productList,
                { product -> customerSearchCatalogController.onProductClick(product) },
                { product -> customerSearchCatalogController.onAddToBasket(product) },
                { product -> customerSearchCatalogController.onRemoveFromBasket(product) },
                { product -> customerSearchCatalogController.onIncrementQuantity(product) }
            )

            recyclerView.adapter = productAdapter

            customerSearchCatalogController = CustomerSearchCatalogController(
                notification,
                productController,
                navigationController
            )

            customerSearchCatalogController.addDataToList(
                requireContext(),
                requireActivity() as AppCompatActivity,
                productAdapter,
                productList,
                searchBar.text.toString()
            )
        } catch (e: Exception) {
            notification.sendNotification("Error while loading search catalog page.", requireActivity() as AppCompatActivity)
            Log.d("CustomerSearchCatalogView", "Error initializing RecyclerView: $e")
        }
    }

    /**
     * Sets up click listeners for UI actions in the CustomerSearchCatalogView.
     *
     * This includes handling clicks on the search bar and product items.
     *
     * @throws Exception if an error occurs while setting up UI actions.
     */
    private fun setUIActions() {
        try {
            customerSearchCatalogController.setupClickListeners(
                searchBar,
                productAdapter,
                productList,
                requireContext(),
                requireActivity() as AppCompatActivity,
            )
        } catch (e: Exception) {
            notification.sendNotification("Error while loading search catalog page.", requireActivity() as AppCompatActivity)
            Log.e("CustomerSearchCatalogView", "Error setting UI actions", e)
        }
    }

    /**
     * Initializes the view components used in the CustomerSearchCatalogView.
     *
     * @param view The root view of the fragment.
     *
     * This method attempts to find the search bar view by its ID and handle any exceptions that may occur.
     * If an exception occurs, it sends a notification to the user and logs the error.
     *
     * @throws Exception if an error occurs while initializing the view components.
     */
    private fun initializeViewComponents(view: View) {
        try {
            searchBar = view.findViewById(R.id.search_bar)
        } catch (e: Exception) {
            notification.sendNotification("Error while loading search catalog page.", requireActivity() as AppCompatActivity)
            Log.e("CustomerSearchCatalogView", "Error initializing view components", e)
        }
    }
}