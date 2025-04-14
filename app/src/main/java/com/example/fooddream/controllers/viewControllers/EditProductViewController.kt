package com.example.fooddream.controllers.viewControllers

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Ingredient

/**
 * EditProductViewController is responsible for managing the edit product view in the application.
 * It handles user interactions, populates ingredients, and sets up click listeners for editing products.
 *
 * @property productController Controller for managing product-related actions.
 * @property notification Notification manager for displaying messages to the user.
 */
class EditProductViewController(
    private val productController: ProductController,
    private val notification: Notification
) {

    /**
     * Populates the ingredient table with existing ingredients for the product.
     *
     * @param context The context of the activity.
     * @param view The activity where the buttons are located.
     * @param ingredientTable The TableLayout to populate with ingredients.
     * @param ingredients The list of ingredients to populate in the table.
     *
     * @throws Exception if an error occurs while populating ingredients.
     */
    @SuppressLint("SetTextI18n")
    fun populateIngredients(
        context: Context,
        view: AppCompatActivity,
        ingredientTable: TableLayout,
        ingredients: List<Ingredient>
    ) {
        try {
            for (ingredient in ingredients) {
                val row = TableRow(context)

                val nameInput = EditText(context).apply {
                    setText(ingredient.getIngredientName())
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    inputType = InputType.TYPE_CLASS_TEXT
                    maxLines = 1
                }

                val weightInput = EditText(context).apply {
                    setText(ingredient.getWeight().toString())
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    maxLines = 1
                }

                val allergensInput = EditText(context).apply {
                    setText(ingredient.getAllergens())
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    inputType = InputType.TYPE_CLASS_TEXT
                    maxLines = 1
                }

                val removeButton = Button(context).apply {
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
            notification.sendNotification("Error while getting ingredients for product.", view)
            Log.e("EditProductView", "Error populating ingredients", e)
        }
    }

    /**
     * Sets up click listeners for the confirm button and ingredient table.
     *
     * @param view The activity where the buttons are located.
     * @param context The context of the activity.
     * @param productId The ID of the product being edited.
     * @param confirmButton The button to confirm the edit.
     * @param imageURL EditText for entering the image URL.
     * @param productNameEditView EditText for entering the product name.
     * @param productDescriptionEditView EditText for entering the product description.
     * @param productPriceEditView EditText for entering the product price.
     * @param productCategoryEditView EditText for entering the product category.
     * @param productStockEditView EditText for entering the product stock.
     * @param productCOEditView EditText for entering the product CO value.
     * @param ingredientTable The TableLayout containing ingredients.
     *
     * @throws Exception if an error occurs while setting up click listeners.
     */
    @SuppressLint("SetTextI18n")
    fun setupClickListeners(
        view: AppCompatActivity,
        context: Context,
        productId: Int,
        confirmButton: Button,
        imageURL: EditText,
        productNameEditView: EditText,
        productDescriptionEditView: EditText,
        productPriceEditView: EditText,
        productCategoryEditView: EditText,
        productStockEditView: EditText,
        productCOEditView: EditText,
        ingredientTable: TableLayout,
    ) {
        try {
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
                        view
                    )
                    return@setOnClickListener
                }

                if (ingredientTable.childCount <= 1) {
                    notification.sendNotification(
                        "Please add at least one ingredient",
                        view
                    )
                    return@setOnClickListener
                }

                if (productPriceEditView.text.toString().toDouble() <= 0) {
                    notification.sendNotification(
                        "Price must be greater than 0",
                        view
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
                        Volley.newRequestQueue(context),
                        notification
                    )
                }
            }
        } catch (e: Exception) {
            notification.sendNotification("Error while loading edit product page.", view)
            Log.e("EditProductView", "Error setting up listeners", e)
        }
    }
}