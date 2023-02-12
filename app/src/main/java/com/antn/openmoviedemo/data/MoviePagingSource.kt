package com.antn.openmoviedemo.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.antn.openmoviedemo.api.MovieApi

class MoviePagingSource(
    private val movieApi: MovieApi,
    private val title: String
): PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = movieApi.searchMovie(title, page)
            val movies = response.search
            if (movies != null) {
                LoadResult.Page(
                    data = movies,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (movies.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Page(
                    data = listOf(),
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = null
                )
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }

    }
}