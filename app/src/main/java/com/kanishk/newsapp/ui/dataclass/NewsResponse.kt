package com.kanishk.newsapp.ui.dataclass

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)