package com.vb.blog.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vb.blog.R
import com.vb.blog.data.model.User
import com.vb.blog.databinding.ActivityProfileBinding
import com.vb.blog.ui.blog.PostsActivity
import com.vb.blog.ui.login.LoginActivity


class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityProfileBinding
    private var database = FirebaseDatabase.getInstance()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listOf(
                getString(R.string.profile_gender_male),
                getString(R.string.profile_gender_female)
            )
        )
        auth = FirebaseAuth.getInstance()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRef = database.getReference(auth.currentUser!!.uid)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { child ->
                    when (child.key) {
                        "firstName" -> binding.profileFirstName.setText(child.getValue(String::class.java))
                        "lastName" -> binding.profileLastName.setText(child.getValue(String::class.java))
                        "age" -> binding.profileAge.setText(child.getValue(String::class.java))
                        "gender" -> {
                            binding.profileGender.setSelection(
                                adapter.getPosition(
                                    child.getValue(
                                        String::class.java
                                    )
                                )
                            )
                        }
                        "username" -> binding.profileUsername.setText(child.getValue(String::class.java))
                        "address" -> binding.profileAdress.setText(child.getValue(String::class.java))
                        "website" -> binding.profileWebsite.setText(child.getValue(String::class.java))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        binding.profileToBlog.setOnClickListener {
            startActivity(Intent(this, PostsActivity::class.java))
        }

        binding.profileEmail.setText(auth.currentUser?.email ?: "")
        binding.profileGender.adapter = adapter
        binding.profileUpdateProfile.setOnClickListener {
            if (Patterns.EMAIL_ADDRESS.matcher(binding.profileEmail.text).matches().not()
                || binding.profileEmail.text.isNullOrEmpty()
            ) {
                binding.profileEmail.error = getString(R.string.registration_invalid_email)
            } else if (binding.profileEmail.text.toString() != auth.currentUser?.email
                && auth.currentUser?.email.isNullOrEmpty().not()
            ) {
                auth.currentUser?.updateEmail(binding.profileEmail.text.toString())
            }
            userRef.setValue(
                User(
                    id = auth.currentUser!!.uid,
                    email = auth.currentUser?.email.toString(),
                    firstName = binding.profileFirstName.text.toString(),
                    lastName = binding.profileLastName.text.toString(),
                    age = binding.profileAge.text.toString(),
                    gender = adapter.getItem(binding.profileGender.selectedItemPosition),
                    address = binding.profileAdress.text.toString(),
                    website = binding.profileWebsite.text.toString(),
                    username = binding.profileUsername.text.toString()
                )
            )
        }
        binding.profileLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
