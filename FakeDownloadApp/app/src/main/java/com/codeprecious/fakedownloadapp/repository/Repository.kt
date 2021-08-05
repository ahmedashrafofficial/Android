package com.codeprecious.fakedownloadapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.codeprecious.fakedownloadapp.model.Data
import com.codeprecious.fakedownloadapp.retrofit.RetrofitApi
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val retrofitApi: RetrofitApi
) {

    fun getData(): LiveData<List<Data>> {
        return LiveDataReactiveStreams.fromPublisher(
            retrofitApi.getFakeData()
                .subscribeOn(Schedulers.io())
        )

    }
}