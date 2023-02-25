package com.vb.blog.ui.blog

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vb.blog.data.model.Post
import com.vb.blog.databinding.ActivityPostsBinding
import com.vb.blog.ui.blog.widget.PostsRecyclerAdapter

class PostsActivity : AppCompatActivity() {
    companion object {
        const val POST_FROM_LIST_TO_DETAIL_ID = "101"
    }

    private lateinit var binding: ActivityPostsBinding
    private val adapter = PostsRecyclerAdapter()
    private val posts = mutableListOf<Post>()
    private var database = FirebaseDatabase.getInstance()
    private val postsRef = database.getReference("posts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.postsRecycler.adapter = adapter
        binding.postsNewPost.setOnClickListener {
            startActivity(Intent(this, CreatePostActivity::class.java))
        }
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                posts.clear()
                snapshot.children.forEach { childrens ->
                    var post = Post("", "", "", null)
                    childrens.children.forEach {
                        when (it.key) {
                            "id" -> {
                                post.id = it.getValue(String::class.java) ?: ""
                            }
                            "author" -> {
                                post.author = it.getValue(String::class.java) ?: ""
                            }
                            "text" -> {
                                post.text = it.getValue(String::class.java) ?: ""
                            }
                        }
                    }
                    if (post.author.isNotEmpty() && post.text.isNotEmpty() && post.id.isNotEmpty()) {
                        posts.add(post)
                        post = Post("", "", "", null)
                    }
                }
                adapter.submitList(posts)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        adapter.onPostClicked = {
            startActivity(Intent(this, PostsDetailActivity::class.java)
                .apply {
                    putExtra(POST_FROM_LIST_TO_DETAIL_ID, it)
                })
        }
    }
}
