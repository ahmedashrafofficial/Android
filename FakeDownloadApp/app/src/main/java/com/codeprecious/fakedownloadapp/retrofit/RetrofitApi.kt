package com.codeprecious.fakedownloadapp.retrofit

import retrofit2.http.GET


interface RetrofitApi {

    companion object {
        const val BASE_URL = "https://nagwa.free.beeceptor.com/"
    }

    @GET("movies")
    fun getFakeData()
}