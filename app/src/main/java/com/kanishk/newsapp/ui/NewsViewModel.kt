package com.kanishk.newsapp.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kanishk.newsapp.ui.dataclass.Article
import com.kanishk.newsapp.ui.dataclass.NewsResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NewsViewModel : ViewModel() {
    var articles by mutableStateOf<List<Article>>(emptyList())
        private set
    var isLoading :Boolean = true
    private val _currentArticle = MutableStateFlow<Article?>(null)
    val currentArticle: StateFlow<Article?> = _currentArticle
    private var newsJob: Job? = null
    fun fetchNews(countryCode: String) {
        viewModelScope.launch {
            articles = getTopHeadlines(countryCode)
            if (articles.isNotEmpty())
                isLoading=false
        }
    }

    fun startAutoLifeCycle(){
        newsJob?.cancel()
        newsJob = viewModelScope.launch(Dispatchers.Default) {
            val articles = getRandomNews() // expensive, keep off main thread


            while (isActive) {
                val batches = articles.shuffled().chunked(10)
                for (batch in batches) {
                    for (article in batch) {
                        _currentArticle.emit(article) // safe flow emission
                        delay(10_000) // yield control
                    }
                }
            }
        }
    }



    suspend fun getTopHeadlines(countryCode: String): List<Article> {
        val apiKey = "3df7c7834b50487b8962e1592e811d5d"
        val url = "https://newsapi.org/v2/top-headlines?category=$countryCode&apiKey=$apiKey"

        return try {
            val response = KtorClient.client.get(url).body<NewsResponse>()
            Log.e("KtorSucess", "${response.articles.size}")
            response.articles
        } catch (e: Exception) {
            Log.e("KtorError", "${e.message} + $url" ?: "Unknown error")
            emptyList()
        }
    }


    suspend fun getRandomNews():List<Article>{
        val url = "https://newsapi.org/v2/everything?q=global&language=en&from=${getPreviousDateLegacy()}&to=${getPreviousDateLegacy()}&sortBy=popularity&apiKey=3df7c7834b50487b8962e1592e811d5d"

        return try {
            val response = KtorClient.client.get(url).body<NewsResponse>()
            Log.e("KtorSucess", "${response.articles.size} Url : $url CurrentDte: ${getPreviousDateLegacy()}")
            response.articles
        } catch (e: Exception) {
            Log.e("KtorError", "${e.message} + $url" ?: "Unknown error")
            emptyList()
        }
    }

    fun getPreviousDateLegacy(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -1) // Subtract 1 day
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(calendar.time)
    }


}
