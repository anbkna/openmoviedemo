package com.antn.openmoviedemo.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.antn.openmoviedemo.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MovieRepository): ViewModel() {

    private val currentSearch = MutableLiveData("Marvel")

    val movies = currentSearch.switchMap {
        repository.getSearchResults(it).cachedIn(viewModelScope)
    }

    fun searchMovies(title: String) {
        currentSearch.value = title
    }

    companion object {
        private const val DEFAULT_SEARCH = "Marvel"
    }
}