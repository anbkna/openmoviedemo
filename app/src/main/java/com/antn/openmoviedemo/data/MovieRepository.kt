package com.antn.openmoviedemo.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.antn.openmoviedemo.api.MovieApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val movieApi: MovieApi) {
    fun getSearchResults(title: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 120,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            MoviePagingSource(movieApi, title)
        }
    ).liveData
}