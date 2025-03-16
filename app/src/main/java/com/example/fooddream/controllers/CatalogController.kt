package com.example.fooddream.controllers

import com.example.fooddream.models.Catalog
import com.example.fooddream.models.Product

class CatalogController (
    private var catalog: Catalog
) {
    fun displayProducts() {

    }
    fun searchProducts(query: String): List<Product>? {
        return null
    }
    fun filterProducts(category: String): List<Product>? {
        return null
    }
    fun viewProduct(productId: Int): Product? {
        return null
    }
}