package com.example.switchcomm.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.switchcomm.data.model.Article
import com.example.switchcomm.data.model.NewsResponse
import com.example.switchcomm.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchNews(query: String = "tesla") {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response: NewsResponse = repository.searchNews(query)
                _articles.value = response.articles ?: emptyList()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun searchNews(query: String) {
        fetchNews(query)
    }
}
