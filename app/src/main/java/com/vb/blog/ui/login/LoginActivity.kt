package com.vb.blog.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.vb.blog.R
import com.vb.blog.databinding.ActivityLoginBinding
import com.vb.blog.ui.profile.ProfileActivity
import com.vb.blog.ui.registration.RegistrationActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton?.setOnClickListener {
            var errorMessage: String? = null
            when {
                binding.username.text.isNullOrEmpty()
                        || binding.password.text.isNullOrEmpty() -> {
                    errorMessage = getString(R.string.registration_empty_fields)
                }
                Patterns.EMAIL_ADDRESS.matcher(binding.username.text).matches().not() -> {
                    errorMessage = getString(R.string.registration_invalid_email)
                }
                binding.password.text.length < 6 -> {
                    errorMessage = getString(R.string.registration_short_password)
                }
            }
            if (errorMessage != null) {
                binding.password.error = errorMessage
            } else {
                auth.signInWithEmailAndPassword(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                ).addOnSuccessListener {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }.addOnFailureListener {
                    binding.password.error = getString(R.string.login_error)
                }
            }
        }
        binding.registrationButton?.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
        binding.forgotYourPasswordButton?.setOnClickListener {
            startActivity(Intent(this, ForgotYourPasswordActivity::class.java))
        }
    }
}
