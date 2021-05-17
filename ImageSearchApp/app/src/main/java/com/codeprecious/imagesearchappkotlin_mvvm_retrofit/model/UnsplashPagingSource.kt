package com.codeprecious.imagesearchappkotlin_mvvm_retrofit.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.retrofit.Api
import retrofit2.HttpException
import retrofit2.http.HTTP
import java.io.IOException

// we are out of the class so no need for companion object
private const val STARTING_INDEX = 1

class UnsplashPagingSource(

    private val UnsplashApi: Api,
    private val query: String
) : PagingSource<Int, UnsplashPhoto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val position = params.key ?: STARTING_INDEX

        return try {
            // data
            val response = UnsplashApi.searchPhotos(query, position, params.loadSize)
            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (position == STARTING_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception) // no live connection when making request
        } catch (exception: HttpException) {
            LoadResult.Error(exception) // something wrong on the server like no authorization to do the request or no data
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        TODO("Not yet implemented")
    }
}