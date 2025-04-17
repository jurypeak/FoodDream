package com.example.fooddream.unitTests.model

import com.example.fooddream.models.Catalog
import com.example.fooddream.models.Product
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CatalogUnitTest {

    private lateinit var product1: Product
    private lateinit var product2: Product
    private lateinit var catalog: Catalog

    @BeforeTest
    fun setUp() {
        product1 = Product("Burger", 1, 5.99, "UK", 2, "", "Food", "")
        product2 = Product("Chicken", 4, 2.99, "USA", 22, "", "Chicken", "")
        catalog = Catalog(products = listOf(product1, product2))
    }

    @Test
    fun `getProducts should return correct product list`() {
        val products = catalog.getProducts()
        assertEquals(2, products.size)
        assertEquals("Burger", products[0].getProductName())
        assertEquals("Chicken", products[1].getProductName())
    }

    @Test
    fun `setProducts should update product list`() {
        val newProduct = Product("Soda", 34, 0.99, "UK", 453, "", "Soda", "")
        catalog.setProducts(listOf(newProduct))

        val updated = catalog.getProducts()
        assertEquals(1, updated.size)
        assertEquals("Soda", updated[0].getProductName())
    }
}
