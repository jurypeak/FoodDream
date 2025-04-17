package com.example.fooddream.unitTests.messenger

import android.app.Activity
import com.example.fooddream.messengers.CustomerSupport
import com.example.fooddream.messengers.Notification
import com.example.fooddream.utils.ValidateManager
import io.mockk.*
import org.junit.Before
import org.junit.Test

class CustomerSupportUnitTest {

    private lateinit var customerSupport: CustomerSupport
    private lateinit var mockNotification: Notification
    private lateinit var mockValidateManager: ValidateManager
    private lateinit var mockActivity: Activity

    @Before
    fun setup() {
        mockNotification = mockk(relaxed = true)
        mockValidateManager = mockk(relaxed = true)
        mockActivity = mockk()

        customerSupport = CustomerSupport().apply {
            this.notification = mockNotification
            this.validateManager = mockValidateManager
        }
    }

    @Test
    fun `submitTicket should send notification if fields are empty`() {
        val email = ""
        val message = ""

        customerSupport.submitTicket(email, message, mockActivity)

        verify { mockNotification.sendNotification("All fields are required", mockActivity) }
    }

    @Test
    fun `submitTicket should send notification if email is invalid`() {
        val email = "invalidEmail"
        val message = "Message"

        every { mockValidateManager.isValidEmail(email) } returns false

        customerSupport.submitTicket(email, message, mockActivity)

        verify { mockNotification.sendNotification("Invalid email address.", mockActivity) }
    }

    @Test
    fun `submitTicket should send notification if message is empty`() {
        val email = "valid@example.com"
        val message = ""

        every { mockValidateManager.isValidEmail(email) } returns true
        every { mockValidateManager.isValidMessage(message) } returns false

        customerSupport.submitTicket(email, message, mockActivity)

        verify { mockNotification.sendNotification("Message cannot be empty.", mockActivity) }
    }

    @Test
    fun `submitTicket should send success notification if email and message are valid`() {
        val email = "valid@example.com"
        val message = "Valid message"

        every { mockValidateManager.isValidEmail(email) } returns true
        every { mockValidateManager.isValidMessage(message) } returns true

        customerSupport.submitTicket(email, message, mockActivity)

        verify { mockNotification.sendNotification("Ticket submitted successfully!", mockActivity) }
    }

    @Test
    fun `submitTicket should handle exception gracefully`() {
        val email = "valid@example.com"
        val message = "Valid message"
        val exception = Exception("Some error")

        every { mockValidateManager.isValidEmail(email) } returns true
        every { mockValidateManager.isValidMessage(message) } returns true
        every { mockNotification.sendNotification(any(), any()) } throws exception

        try {
            customerSupport.submitTicket(email, message, mockActivity)
        } catch (e: Exception) {
            verify { mockNotification.sendNotification("Error occurred while submitting the ticket.", mockActivity) }
        }
    }
}
