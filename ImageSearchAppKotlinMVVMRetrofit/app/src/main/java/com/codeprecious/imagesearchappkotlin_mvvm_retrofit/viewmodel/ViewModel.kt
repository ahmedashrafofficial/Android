package com.codeprecious.imagesearchappkotlin_mvvm_retrofit.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.filter
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.model.UnsplashPhoto
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.repository.Repository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repository: Repository,
    state: SavedStateHandle
) : ViewModel() {

    //    private val currentQuery = MutableLiveData(DEFAULT_QUERY)
    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)
    val photos =
        currentQuery.switchMap { query ->  // when currentQuery changes, switchMap triggers & gets the new value as livedata
            repository.getSearchResults(query)
                .cachedIn(viewModelScope) // to survive the screen orientation changes
        }

    fun searchPhoto(query: String) {
        currentQuery.value = query
    }

    private val _g = MutableLiveData<PagingData<UnsplashPhoto>>()
    fun g(): LiveData<PagingData<UnsplashPhoto>> {
        return _g
    }

    fun getPhotos(query: String) {
        viewModelScope.launch {
            repository.get(query).cachedIn(viewModelScope).collect {
                _g.value = it
            }
        }
    }

    companion object {
        const val CURRENT_QUERY = "current_query"
        const val DEFAULT_QUERY = "kitten"
    }
}