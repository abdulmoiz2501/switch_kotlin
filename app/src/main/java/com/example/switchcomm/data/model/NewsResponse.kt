package com.example.switchcomm.data.model

data class NewsResponse(
    val status: String?,
    val totalResults: Int?,
    val articles: List<Article>?
)
