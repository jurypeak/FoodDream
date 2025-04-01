package com.example.fooddream.controllers

import com.example.fooddream.interfaces.ICatalogController
import com.example.fooddream.models.Catalog
import com.example.fooddream.models.Product

class AdminCatalogController (
    private var catalog: Catalog,
): ICatalogController {

    fun addProduct(newProduct: Product) {

    }
    fun deleteProduct(productId: Int) {

    }
    fun editProduct(
        newName: String,
        newCategory: String,
        newPrice: Double,
        newDescription: String,
        newImageURL: String
    ) {
    }
    override fun displayProducts() {
        TODO("Not yet implemented")
    }

    override fun searchProducts(query: String): List<Product>? {
        TODO("Not yet implemented")
    }

    override fun filterProducts(category: String): List<Product>? {
        TODO("Not yet implemented")
    }

    override fun viewProduct(productId: Int): Product? {
        TODO("Not yet implemented")
    }
}