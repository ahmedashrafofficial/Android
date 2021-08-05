package com.codeprecious.fakedownloadapp.retrofit

import com.codeprecious.fakedownloadapp.model.Data
import retrofit2.http.GET


interface RetrofitApi {

    companion object {
        const val BASE_URL = "https://nagwa.free.beeceptor.com/"
    }

    @GET("movies")
    fun getFakeData(): List<Data>
}