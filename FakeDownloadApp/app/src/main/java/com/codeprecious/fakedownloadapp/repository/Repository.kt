package com.codeprecious.fakedownloadapp.repository

import com.codeprecious.fakedownloadapp.model.Data
import com.codeprecious.fakedownloadapp.retrofit.RetrofitApi
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val retrofitApi: RetrofitApi
) {

    fun getData(): Flowable<List<Data>> {
        return retrofitApi.getFakeData()
    }
}