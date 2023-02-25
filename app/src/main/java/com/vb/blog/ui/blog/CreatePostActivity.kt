package com.vb.blog.ui.blog

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vb.blog.R
import com.vb.blog.data.model.Post
import com.vb.blog.databinding.ActivityNewPostBinding

class CreatePostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPostBinding
    private lateinit var auth: FirebaseAuth
    var database = FirebaseDatabase.getInstance()
    val postsRef = database.getReference("posts")
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userRef = database.getReference(auth.currentUser!!.uid)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { child ->
                    if (child.key == "username") {
                        username = child.getValue(String::class.java)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        binding.newPostSave.setOnClickListener {
            if (binding.newPostText.text.isNullOrEmpty().not()) {
                val postId = auth.currentUser!!.uid + System.currentTimeMillis()
                postsRef.child(postId)
                    .setValue(
                        Post(
                            id = postId,
                            author = username ?: auth.currentUser!!.email!!,
                            text = binding.newPostText.text.toString(),
                            null
                        )
                    ).addOnSuccessListener {
                        startActivity(Intent(this, PostsActivity::class.java))
                    }
            } else {
                binding.newPostText.error = getString(R.string.new_post_empty_field)
            }
        }

    }
}
