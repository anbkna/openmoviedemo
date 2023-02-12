package com.antn.openmoviedemo.api

import com.antn.openmoviedemo.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    companion object {
        const val BASE_URL = "https://www.omdbapi.com/"
        const val API_KEY = BuildConfig.API_KEY
    }

    @GET("/?apikey=$API_KEY")
    suspend fun searchMovie(
        @Query("s")
        title: String,
        @Query("page")
        page: Int,
        @Query("type")
        type: String = "movie"
    ): SearchResponse
}