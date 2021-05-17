package com.codeprecious.imagesearchappkotlin_mvvm_retrofit.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.model.UnsplashPagingSource
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.retrofit.Api
import javax.inject.Inject

class Repository @Inject constructor(
    private val retrofitApi: Api
) {

    fun getSearchResults(query: String) =
        Pager(
            // visible items on screeen, max number of loaded items at scrolling
            config = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false),
            pagingSourceFactory = { UnsplashPagingSource(retrofitApi, query) }
        ).liveData

    fun get(query: String) =
        Pager(
            config = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false),
            pagingSourceFactory = { UnsplashPagingSource(retrofitApi, query) }
        ).flow

}