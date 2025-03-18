package com.example.fooddream.controllers

import com.example.fooddream.interfaces.ICatalogController
import com.example.fooddream.models.Catalog
import com.example.fooddream.models.Product

class CustomerCatalogController(
    private var catalog: Catalog
): ICatalogController {
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