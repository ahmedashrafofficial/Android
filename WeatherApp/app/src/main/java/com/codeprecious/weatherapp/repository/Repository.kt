package com.codeprecious.weatherapp.repository

import com.codeprecious.weatherapp.model.City
import com.codeprecious.weatherapp.model.Weather
import com.codeprecious.weatherapp.retrofit.RetrofitApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class Repository @Inject constructor(
    private val retrofitApi: RetrofitApi
) {
    suspend fun getLocationKey(query: String): Flow<List<City>> = flow {
        emit(retrofitApi.getLocationKey(query = query))
    }

    suspend fun getLocationKeyByLatLong(query: String): Flow<List<City>> = flow {
        emit(retrofitApi.getLocationKey(query = query))
    }

    suspend fun getWeather(key: String, metric: Boolean): Flow<Weather> = flow {
        emit(retrofitApi.getWeather(locationKey = key, metric = metric))
    }
}