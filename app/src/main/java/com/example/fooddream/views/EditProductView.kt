package com.example.fooddream.views

import com.example.fooddream.repositories.ProductRepository
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fooddream.R
import com.example.fooddream.controllers.viewControllers.EditProductViewController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.repositories.IngredientRepository

/**
 * EditProductView is a Fragment that allows the admin to edit an existing product.
 * It contains fields for product details and a button to submit the changes.
 *
 * @property ingredientTable TableLayout for displaying ingredients.
 * @property confirmButton Button to confirm the changes.
 * @property imageURL EditText for entering the product image URL.
 * @property productNameEditView EditText for entering the product name.
 * @property productDescriptionEditView EditText for entering the product description.
 * @property productPriceEditView EditText for entering the product price.
 * @property productCategoryEditView EditText for entering the product category.
 * @property productStockEditView EditText for entering the product stock.
 * @property productCOEditView EditText for entering the product CO (Country of Origin).
 *
 * @property productController Controller for managing product-related actions.
 * @property navigationController Controller for managing navigation actions.
 * @property editProductViewController Controller for managing the edit product view logic.
 * @property ingredientRepository Repository for managing ingredients.
 * @property notification Notification manager for displaying messages to the user.
 */
class EditProductView : Fragment() {
    private lateinit var ingredientTable: TableLayout
    private lateinit var confirmButton: Button
    private lateinit var imageURL: EditText
    private lateinit var productNameEditView: EditText
    private lateinit var productDescriptionEditView: EditText
    private lateinit var productPriceEditView: EditText
    private lateinit var productCategoryEditView: EditText
    private lateinit var productStockEditView: EditText
    private lateinit var productCOEditView: EditText

    private lateinit var productController: ProductController
    private lateinit var navigationController: NavigationController
    private lateinit var editProductViewController: EditProductViewController
    private lateinit var productRepository: ProductRepository
    private lateinit var ingredientRepository: IngredientRepository
    private lateinit var notification: Notification

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_product_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    /**
     * Initializes the EditProductView by setting up controllers, view components, and UI actions.
     *
     * @param view The root view of the fragment.
     */
    private fun init(view: View) {
        initializeControllers(requireActivity() as AppCompatActivity)
        initializeViewComponents(view)
        setUIActions()
    }

    /**
     * Initializes the controllers used in the EditProductView.
     *
     * @param view The activity context.
     *
     * This method attempts to create instances of the ProductController, NavigationController,
     * ProductRepository, IngredientRepository, and Notification classes.
     * If an exception occurs, it sends a notification to the user and logs the error.
     *
     * @throws Exception if an error occurs while initializing the controllers.
     */
    private fun initializeControllers(view: AppCompatActivity) {
        try {
            productController = ProductController(view)
            navigationController = NavigationController(view)
            productRepository = ProductRepository(view)
            ingredientRepository = IngredientRepository(view)
            notification = Notification()

            editProductViewController = EditProductViewController(
                productController,
                notification
            )
        } catch (e: Exception) {
            notification.sendNotification("Error while initializing controllers.", view)
            Log.e("EditProductView", "Error initializing controllers", e)
        }
    }

    /**
     * Sets up the UI actions for the edit product view.
     * This method configures click listeners for various UI components.
     *
     * @throws Exception if an error occurs while setting up UI actions.
     */
    private fun setUIActions() {
        try {
            var productId = arguments?.getInt("ProductId") ?: -1
            editProductViewController.setupClickListeners(
                requireActivity() as AppCompatActivity,
                requireContext(),
                productId,
                confirmButton,
                imageURL,
                productNameEditView,
                productDescriptionEditView,
                productPriceEditView,
                productCategoryEditView,
                productStockEditView,
                productCOEditView,
                ingredientTable,
            )
        } catch (e: Exception) {
            notification.sendNotification("Error while setting UI actions.", requireActivity() as AppCompatActivity)
            Log.e("EditProductView", "Error setting UI actions", e)
        }
    }

    /**
     * Initializes the view components used in the EditProductView.
     *
     * @param view The root view of the fragment.
     *
     * This method attempts to set up the UI components for editing a product.
     * If an exception occurs, it sends a notification to the user and logs the error.
     *
     * @throws Exception if an error occurs while initializing the view components.
     */
    private fun initializeViewComponents(view: View) {
        try {
            var productId = arguments?.getInt("ProductId") ?: -1

            ingredientTable = view.findViewById(R.id.ingredient_table)

            confirmButton = view.findViewById(R.id.editButton)

            imageURL = view.findViewById(R.id.imageURL_edit_product)
            productNameEditView = view.findViewById(R.id.productName_edit_product)
            productDescriptionEditView = view.findViewById(R.id.productDescription_edit_product)
            productPriceEditView = view.findViewById(R.id.productPrice_edit_product)
            productCategoryEditView = view.findViewById(R.id.productCategory_edit_product)
            productStockEditView = view.findViewById(R.id.productStock_edit_product)
            productCOEditView = view.findViewById(R.id.productCO_edit_product)

            val product = productRepository.getProduct(productId)

            product?.let {
                imageURL.setText(it.getImageUrl().toString())
                productNameEditView.setText(it.getProductName())
                productDescriptionEditView.setText(it.getProductDescription())
                productPriceEditView.setText(it.getProductPrice().toString())
                productCategoryEditView.setText(it.getProductCategory())
                productStockEditView.setText(it.getProductStock().toString())
                productCOEditView.setText(it.getProductCO())

                val ingredients = ingredientRepository.getIngredients(productId)
                editProductViewController.populateIngredients(
                    requireContext(),
                    requireActivity() as AppCompatActivity,
                    ingredientTable,
                    ingredients,
                )
            }
        } catch (e: Exception) {
            notification.sendNotification("Error while loading edit product page.", requireActivity() as AppCompatActivity)
            Log.e("EditProductView", "Error initializing view components", e)
        }
    }
}