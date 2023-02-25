package com.vb.blog.data.model

data class Post(var id: String, var author: String, var text: String, var comments: List<String>?)
