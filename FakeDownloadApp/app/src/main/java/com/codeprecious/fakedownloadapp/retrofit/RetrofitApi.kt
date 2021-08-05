package com.codeprecious.fakedownloadapp.retrofit

import com.codeprecious.fakedownloadapp.model.Data
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET


interface RetrofitApi {

    companion object {
        const val BASE_URL = "https://nagwa.free.beeceptor.com/"
    }

    @GET("movies")
    fun getFakeData(): Flowable<List<Data>>
}