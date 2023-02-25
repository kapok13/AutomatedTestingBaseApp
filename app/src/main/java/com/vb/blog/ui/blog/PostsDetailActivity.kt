package com.vb.blog.ui.blog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vb.blog.R
import com.vb.blog.data.model.Comment
import com.vb.blog.databinding.ActivityPostsDetailBinding
import com.vb.blog.hideKeyboard
import com.vb.blog.ui.blog.widget.CommentsRecyclerAdapter

class PostsDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostsDetailBinding
    private lateinit var auth: FirebaseAuth
    private val adapter = CommentsRecyclerAdapter()
    private val comments = mutableListOf<Comment>()
    private var database = FirebaseDatabase.getInstance()
    private val postsRef = database.getReference("posts")
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
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
        binding = ActivityPostsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.postsDetailCommentsRecycler.adapter = adapter
        val id = intent.getStringExtra(PostsActivity.POST_FROM_LIST_TO_DETAIL_ID)!!

        postsRef.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                comments.clear()
                snapshot.children.forEach { child ->
                    var comment = Comment("", "")
                    when (child.key) {
                        "author" -> binding.postsDetailAuthor.text =
                            child.getValue(String::class.java)
                        "text" -> binding.postsDetailText.text = child.getValue(String::class.java)
                        "comments" -> {
                            child.children.forEach { commentChild ->
                                commentChild.children.forEach {
                                    when (it.key) {
                                        "author" -> comment.author =
                                            it.getValue(String::class.java) ?: ""
                                        "text" -> comment.text = it.getValue(String::class.java) ?: ""
                                    }
                                }
                                if (comment.author.isNotEmpty() && comment.text.isNotEmpty()) {
                                    comments.add(comment)
                                    comment = Comment("", "")
                                }
                            }
                        }
                    }
                }
                adapter.submitList(comments)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        binding.postsDetailSend.setOnClickListener {
            if (binding.postsDetailComment.text.isNullOrEmpty().not()) {
                val commentId = auth.currentUser!!.uid + System.currentTimeMillis()
                postsRef.child(id).child("comments").child(commentId)
                    .setValue(
                        Comment(
                            author = username ?: auth.currentUser!!.email!!,
                            text = binding.postsDetailComment.text.toString()
                        )
                    ).addOnCompleteListener {
                        binding.postsDetailComment.text = null
                        binding.root.hideKeyboard()
                    }
            } else {
                binding.postsDetailComment.error = getString(R.string.new_post_empty_comment)
            }
        }
    }
}
