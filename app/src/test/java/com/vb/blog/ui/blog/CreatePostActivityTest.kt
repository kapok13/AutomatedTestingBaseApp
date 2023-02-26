package com.vb.blog.ui.blog

import junit.framework.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class CreatePostActivityTest {

    @Test
    fun onCreate() {
        val postText = listOf("blablabla", "")

        assertTrue(validatePostText(postText[0]))
        assertFalse(validatePostText(postText[1]))
    }

    private fun validatePostText(postText: String?) = postText.isNullOrEmpty().not()

}
