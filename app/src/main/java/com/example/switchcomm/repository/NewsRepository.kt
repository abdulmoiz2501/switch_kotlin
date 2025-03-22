package com.example.switchcomm.repository

import com.example.switchcomm.data.api.RetrofitInstance
import com.example.switchcomm.data.model.NewsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepository {

    private val apiKey = "26eddb704342490ab236eaeab5045340"

    suspend fun searchNews(query: String): NewsResponse {
        return RetrofitInstance.api.searchNews(query, apiKey)
    }

    fun searchNewsFlow(query: String): Flow<NewsResponse> = flow {
        val response = RetrofitInstance.api.searchNews(query, apiKey)
        emit(response)
    }
}
