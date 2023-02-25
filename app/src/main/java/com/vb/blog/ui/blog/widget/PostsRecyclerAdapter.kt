package com.vb.blog.ui.blog.widget

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.vb.blog.data.model.Post

class PostsRecyclerAdapter : ListAdapter<Post, PostsViewHolder>(
    object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.text == newItem.text && oldItem.author == newItem.author
        }
    }
) {
    var onPostClicked: ((s: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder =
        PostsViewHolder(PostItemView(parent.context))

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        with(holder.itemView as PostItemView) {
            setItem(getItem(position))
            holder.itemView.setOnClickListener {
                onPostClicked?.invoke(getItem(position).id)
            }
        }
    }
}
