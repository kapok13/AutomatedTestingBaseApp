package com.vb.blog.ui.registration

import androidx.core.util.PatternsCompat
import com.google.firebase.auth.FirebaseAuth
import junit.framework.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.mockito.Mockito

internal class RegistrationActivityTest {

    private val emails = listOf("correct@gmail.com", "123123123", "ill@gmail", "ill@.com", "duplicate@gmail.com")
    private val passwords = listOf("1", "happyyyyyyyy")
    private val registeredEmail = "duplicate@gmail.com"

    @Test
    fun onCreate() {
        val auth: FirebaseAuth = Mockito.mock(FirebaseAuth::class.java)
        Mockito.`when`(auth.createUserWithEmailAndPassword(registeredEmail, passwords[0])
        ).thenThrow(IllegalArgumentException::class.java)
        Mockito.`when`(auth.createUserWithEmailAndPassword(emails[0], passwords[1])
        ).thenThrow(IllegalStateException::class.java)

        assertEquals(validateEmailAndPassword(emails[0], passwords[0]), "Invalid password")
        assertEquals(validateEmailAndPassword(emails[0], passwords[1]), null)
        assertEquals(validateEmailAndPassword(emails[1], passwords[1]), "Invalid email")
        assertEquals(validateEmailAndPassword(emails[2], passwords[1]), "Invalid email")
        assertEquals(validateEmailAndPassword(emails[3], passwords[1]), "Invalid email")
        assertEquals(validateEmailAndPassword(emails[1], passwords[0]), "Invalid email")
        assertEquals(validateEmailAndPassword(null, passwords[1]), "Email or password is empty")

        assertThrows(IllegalArgumentException::class.java) {
            auth.createUserWithEmailAndPassword(registeredEmail, passwords[0])
        }
        assertThrows(IllegalStateException::class.java) {
            auth.createUserWithEmailAndPassword(emails[0], passwords[1])
        }
    }

    private fun validateEmailAndPassword(email: String?, password: String?) : String? {
        return when {
            password.isNullOrEmpty()
                    || email.isNullOrEmpty() -> "Email or password is empty"
            PatternsCompat.EMAIL_ADDRESS.matcher(email).matches().not() -> "Invalid email"
            password.length < 6 -> "Invalid password"
            else -> null
        }
    }
}
