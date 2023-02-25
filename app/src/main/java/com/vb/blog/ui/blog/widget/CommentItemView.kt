package com.vb.blog.ui.blog.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.vb.blog.data.model.Comment
import com.vb.blog.databinding.ItemCommentBinding

class CommentItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = ItemCommentBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        layoutParams = binding.root.layoutParams
    }

    fun setItem(comment: Comment) {
        binding.itemCommentComment.text = "${comment.author} ${comment.text}"
    }
}
