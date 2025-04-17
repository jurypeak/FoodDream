package com.example.fooddream.unitTests.utils

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.utils.SessionManager
import io.mockk.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SessionManagerUnitTest {

    private lateinit var mockActivity: AppCompatActivity
    private lateinit var mockPrefs: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor
    private lateinit var sessionManager: SessionManager

    @Before
    fun setup() {
        mockActivity = mockk()
        mockPrefs = mockk(relaxed = true)
        mockEditor = mockk(relaxed = true)

        every { mockActivity.getSharedPreferences("user_session", any()) } returns mockPrefs
        every { mockPrefs.edit() } returns mockEditor
        every { mockEditor.putBoolean(any(), any()) } returns mockEditor
        every { mockEditor.remove(any()) } returns mockEditor
        every { mockEditor.apply() } just Runs

        sessionManager = SessionManager(mockActivity)
    }

    @Test
    fun `startSession should save login state true`() {
        sessionManager.startSession(true)

        verify {
            mockEditor.putBoolean("isLoggedIn", true)
            mockEditor.apply()
        }
    }

    @Test
    fun `hasSession should return true if stored value is true`() {
        every { mockPrefs.getBoolean("isLoggedIn", false) } returns true

        val result = sessionManager.hasSession()

        assertTrue(result)
    }

    @Test
    fun `hasSession should return false if stored value is false`() {
        every { mockPrefs.getBoolean("isLoggedIn", false) } returns false

        val result = sessionManager.hasSession()

        assertFalse(result)
    }

    @Test
    fun `clearSession should remove login state`() {
        sessionManager.clearSession()

        verify {
            mockEditor.remove("isLoggedIn")
            mockEditor.apply()
        }
    }
}

