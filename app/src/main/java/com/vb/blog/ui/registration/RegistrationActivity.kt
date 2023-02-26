package com.vb.blog.ui.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.PatternsCompat
import com.google.firebase.auth.FirebaseAuth
import com.vb.blog.R
import com.vb.blog.databinding.ActivityRegistrationBinding
import com.vb.blog.ui.profile.ProfileActivity

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registrationRegistrationButton.setOnClickListener {
            var errorMessage: String? = null
            when {
                binding.registrationPassword.text.isNullOrEmpty()
                        || binding.registrationUsername.text.isNullOrEmpty() -> {
                    errorMessage = getString(R.string.registration_empty_fields)
                }
                PatternsCompat.EMAIL_ADDRESS.matcher(binding.registrationUsername.text).matches().not() -> {
                    errorMessage = getString(R.string.registration_invalid_email)
                }
                binding.registrationPassword.text.length < 6 -> {
                    errorMessage = getString(R.string.registration_short_password)
                }
            }
            if (errorMessage != null) {
                binding.registrationPassword.error = errorMessage
            } else {
                auth.createUserWithEmailAndPassword(
                    binding.registrationUsername.text.toString(),
                    binding.registrationPassword.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this, ProfileActivity::class.java))
                    } else {
                        binding.registrationPassword.error = getString(R.string.registration_already_registered)
                    }
                }
            }
        }
    }
}
