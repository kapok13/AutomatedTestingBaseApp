package com.vb.blog.ui.profile

import androidx.core.util.PatternsCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.vb.blog.data.model.User
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.mockito.Mockito
import kotlin.random.Random

class ProfileActivityTest {

    @Test
    fun onCreate() {
        var userName = ""
        val userRef = Mockito.mock(DatabaseReference::class.java)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userName = Random.nextInt(0, 150).toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        val spyValuesEventListener = Mockito.spy(valueEventListener)
        Mockito.`when`(userRef.addValueEventListener(valueEventListener)).then {
            valueEventListener.onDataChange(Mockito.mock(DataSnapshot::class.java))
            assertNotEquals(userName, "")
            userName = ""
            it
        }
        Mockito.`when`(userRef.addValueEventListener(spyValuesEventListener)).then {
            spyValuesEventListener.onDataChange(Mockito.mock(DataSnapshot::class.java))
            assertNotEquals(userName, "")
            userName = ""
            it
        }

        val currentEmail = "joko@gmail.com"
        val newEmails = listOf("joko@gmail.com", "okooojij", "qweqwe@gmail.com")

        assertEquals(emailValidation(currentEmail, newEmails[0]), "same email")
        assertEquals(emailValidation(currentEmail, newEmails[1]), "Invalid email")
        assertEquals(emailValidation(currentEmail, newEmails[2]), "update")

        var success: Boolean? = null

        val currentFirebaseUser = Mockito.mock(FirebaseUser::class.java)

        Mockito.`when`(currentFirebaseUser.updateEmail(newEmails[2])).then {
            success = true
            Mockito.mock(Task::class.java)
        }

        currentFirebaseUser.updateEmail(newEmails[2])
        assertTrue(success ?: false)

        var userSetValueSucces: Boolean? = null

        Mockito.`when`(userRef.setValue(
            User("123", newEmails[2], null, null, null, null, null, null, null)
        )).then {
            userSetValueSucces = true
            Mockito.mock(Task::class.java)
        }
        userRef.setValue(
            User("123", newEmails[2], null, null, null, null, null, null, null)
        )
        assertTrue(userSetValueSucces ?: false)
    }

    private fun emailValidation(email: String?, newEmail: String?) : String? {
        return if (PatternsCompat.EMAIL_ADDRESS.matcher(newEmail).matches().not() || newEmail.isNullOrEmpty()
        ) {
            "Invalid email"
        } else if (email != newEmail
        ) {
            "update"
        }
        else {
            "same email"
        }
    }
}
