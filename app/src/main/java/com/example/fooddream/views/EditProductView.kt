package com.example.fooddream.views

import com.example.fooddream.repositories.ProductRepository
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.Volley
import com.example.fooddream.R
import com.example.fooddream.BuildConfig
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Ingredient
import com.example.fooddream.repositories.IngredientRepository

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

        productController = ProductController(requireActivity() as AppCompatActivity)
        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        productRepository = ProductRepository(requireActivity() as AppCompatActivity)
        ingredientRepository = IngredientRepository(requireActivity() as AppCompatActivity)
        notification = Notification()

        init(view)
    }

    private fun init(view: View) {
        initializeViewComponents(view)
        setUpListeners()
    }

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
                populateIngredients(ingredients)
            }
        } catch (e: Exception) {
            notification.sendNotification("Error while loading edit product page.", requireActivity() as AppCompatActivity)
            Log.e("EditProductView", "Error initializing view components", e)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun populateIngredients(ingredients: List<Ingredient>) {
        try {
            for (ingredient in ingredients) {
                val row = TableRow(requireContext())

                val nameInput = EditText(requireContext()).apply {
                    setText(ingredient.getIngredientName())
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    inputType = InputType.TYPE_CLASS_TEXT
                    maxLines = 1
                }

                val weightInput = EditText(requireContext()).apply {
                    setText(ingredient.getWeight().toString())
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    maxLines = 1
                }

                val allergensInput = EditText(requireContext()).apply {
                    setText(ingredient.getAllergens())
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    inputType = InputType.TYPE_CLASS_TEXT
                    maxLines = 1
                }

                val removeButton = Button(requireContext()).apply {
                    text = "Remove"
                    layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
                    setOnClickListener {
                        ingredientTable.removeView(row)
                    }
                }

                row.addView(nameInput)
                row.addView(weightInput)
                row.addView(allergensInput)
                row.addView(removeButton)

                ingredientTable.addView(row)
            }
        } catch (e: Exception) {
            notification.sendNotification("Error while getting ingredients for product.", requireActivity() as AppCompatActivity)
            Log.e("EditProductView", "Error populating ingredients", e)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setUpListeners() {
        try {
            var productId = arguments?.getInt("ProductId") ?: -1

            confirmButton.setOnClickListener {
                if (
                    imageURL.text.isEmpty() ||
                    productNameEditView.text.isEmpty() ||
                    productDescriptionEditView.text.isEmpty() ||
                    productPriceEditView.text.isEmpty() ||
                    productCategoryEditView.text.isEmpty() ||
                    productStockEditView.text.isEmpty() ||
                    productCOEditView.text.isEmpty()
                ) {
                    notification.sendNotification(
                        "Please fill in all fields",
                        requireActivity() as AppCompatActivity
                    )
                    return@setOnClickListener
                }

                if (ingredientTable.childCount <= 1) {
                    notification.sendNotification(
                        "Please add at least one ingredient",
                        requireActivity() as AppCompatActivity
                    )
                    return@setOnClickListener
                }

                if (productPriceEditView.text.toString().toDouble() <= 0) {
                    notification.sendNotification(
                        "Price must be greater than 0",
                        requireActivity() as AppCompatActivity
                    )
                    return@setOnClickListener
                }
                else {
                    productController.editProduct(
                        productId,
                        imageURL.text.toString(),
                        productNameEditView.text.toString(),
                        productDescriptionEditView.text.toString(),
                        productPriceEditView.text.toString().toDouble(),
                        productCategoryEditView.text.toString(),
                        productStockEditView.text.toString().toInt(),
                        productCOEditView.text.toString(),
                        productController.collectIngredients(ingredientTable) ?: return@setOnClickListener,
                        BuildConfig.URL_UPDATE_PRODUCT,
                        BuildConfig.URL_UPDATE_INGREDIENT,
                        Volley.newRequestQueue(requireContext()),
                        notification
                    )
                }
            }
        } catch (e: Exception) {
            notification.sendNotification("Error while loading edit product page.", requireActivity() as AppCompatActivity)
            Log.e("EditProductView", "Error setting up listeners", e)
        }
    }
}