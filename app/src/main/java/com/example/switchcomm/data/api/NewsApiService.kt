package com.example.switchcomm.data.api

import com.example.switchcomm.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}
