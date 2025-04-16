package com.example.fooddream.model

import com.example.fooddream.models.Product
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ProductUnitTest {

    private lateinit var product: Product

    @BeforeTest
    fun setUp() {
        product = Product(
            productName = "Organic Apple",
            productId = 1,
            productPrice = 1.99,
            productCO = "USA",
            productStock = 50,
            productDescription = "Fresh organic apples",
            productCategory = "Fruits",
            imageUrl = "http://example.com/apple.jpg"
        )
    }

    @Test
    fun `getters should return correct values`() {
        assertEquals("Organic Apple", product.getProductName())
        assertEquals(1, product.getProductId())
        assertEquals(1.99, product.getProductPrice())
        assertEquals("USA", product.getProductCO())
        assertEquals(50, product.getProductStock())
        assertEquals("Fresh organic apples", product.getProductDescription())
        assertEquals("Fruits", product.getProductCategory())
        assertEquals("http://example.com/apple.jpg", product.getImageUrl())
    }

    @Test
    fun `setters should update fields correctly`() {
        product.setProductName("Banana")
        product.setProductId(2)
        product.setProductPrice(0.99)
        product.setProductCO("Ecuador")
        product.setProductStock(100)
        product.setProductDescription("Ripe bananas")
        product.setProductCategory("Produce")
        product.setImageUrl("http://example.com/banana.jpg")

        assertEquals("Banana", product.getProductName())
        assertEquals(2, product.getProductId())
        assertEquals(0.99, product.getProductPrice())
        assertEquals("Ecuador", product.getProductCO())
        assertEquals(100, product.getProductStock())
        assertEquals("Ripe bananas", product.getProductDescription())
        assertEquals("Produce", product.getProductCategory())
        assertEquals("http://example.com/banana.jpg", product.getImageUrl())
    }
}
