package com.codeprecious.imagesearchappkotlin_mvvm_retrofit.retrofit

import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.model.UnsplashResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Api {

    companion object {  // like static
        const val BASE_URL = "https://api.unsplash.com/"
        const val CLIENT_ID = "2CVZzvQDi_JMRyKqEvKxVOUXWifm6izlqeCT4kmGark"
    }

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): UnsplashResponse
}