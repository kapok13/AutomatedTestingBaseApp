package com.vb.blog.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.vb.blog.R
import com.vb.blog.databinding.ActivityForgotPasswordBinding

class ForgotYourPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resetPasswordButton.setOnClickListener {
            if (Patterns.EMAIL_ADDRESS.matcher(binding.resetUsername.text).matches().not()
                || binding.resetUsername.text.isNullOrEmpty()
            ) {
                binding.resetUsername.error = getString(R.string.registration_invalid_email)
            } else {
                auth.sendPasswordResetEmail(binding.resetUsername.text.toString())
                    .addOnCompleteListener {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
            }
        }
    }
}
