package com.example.switchcomm.ui.viewmodel

import androidx.lifecycle.*
import com.example.switchcomm.data.model.Article
import com.example.switchcomm.data.model.NewsResponse
import com.example.switchcomm.repository.NewsRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /// Updated fetchNews function  using Flow
    fun fetchNews(query: String = "tesla") {
        viewModelScope.launch {
            _loading.value = true
            repository.searchNewsFlow(query)
                .catch { e: Throwable ->
                    _error.value = e.message
                }
                .collect { response: NewsResponse ->
                    _articles.value = response.articles ?: emptyList()
                    _error.value = null
                }
            _loading.value = false
        }
    }

    fun searchNews(query: String) {
        fetchNews(query)
    }
}
