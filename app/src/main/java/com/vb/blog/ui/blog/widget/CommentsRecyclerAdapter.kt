package com.vb.blog.ui.blog.widget

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.vb.blog.data.model.Comment

class CommentsRecyclerAdapter : ListAdapter<Comment, CommentsViewHolder>(
    object : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.text == newItem.text && oldItem.author == newItem.author
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.text == newItem.text && oldItem.author == newItem.author
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder =
        CommentsViewHolder(CommentItemView(parent.context))

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        with(holder.itemView as CommentItemView) {
            setItem(getItem(position))
        }
    }
}
