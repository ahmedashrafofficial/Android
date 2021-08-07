package com.codeprecious.fakedownloadapp.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import com.codeprecious.fakedownloadapp.model.Data
import com.codeprecious.fakedownloadapp.retrofit.RetrofitApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val retrofitApi: RetrofitApi,
    @ApplicationContext private val context: Context
) {

    fun getData(): LiveData<List<Data>> {
        return LiveDataReactiveStreams.fromPublisher(
            retrofitApi.getFakeData().subscribeOn(Schedulers.io()).onErrorReturn {
                getDataFromJSON()
            }
        )
    }

    private fun getDataFromJSON(): List<Data> {
        val gson = Gson()
        val listData = object : TypeToken<List<Data>>() {}.type
        return gson.fromJson(getJSONDataFromAsset(), listData)
    }

    private fun getJSONDataFromAsset(): String? {
        return try {
            context.assets.open("getListOfFilesResponse.json")
                .bufferedReader().use {
                    it.readText()
                }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}