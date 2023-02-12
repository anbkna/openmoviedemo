package com.antn.openmoviedemo.api

import com.antn.openmoviedemo.data.Movie
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("Search")
    val search: List<Movie>?,
    @SerializedName("Response")
    val response: Boolean,
    val totalResults: Int?,
    @SerializedName("Error")
    val error: String?
)