package com.vb.blog.ui.blog.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.vb.blog.data.model.Post
import com.vb.blog.databinding.ItemPostBinding

class PostItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = ItemPostBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        layoutParams = binding.root.layoutParams
    }

    fun setItem(item: Post) {
        binding.postsDetailAuthor.text = item.author
        binding.postsDetailText.text = item.text
    }
}
