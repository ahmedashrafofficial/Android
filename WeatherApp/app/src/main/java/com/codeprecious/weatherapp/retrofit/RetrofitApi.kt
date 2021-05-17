package com.codeprecious.weatherapp.retrofit

import com.codeprecious.weatherapp.model.City
import com.codeprecious.weatherapp.model.Weather
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitApi {

    companion object {
        private const val API_KEY = "JIKe7jUxx8cjGnuhGoVgkoOj7D2Z4XM1"
        const val BASE_URL = "http://dataservice.accuweather.com/"
    }

    @GET("locations/v1/cities/search")
    suspend fun getLocationKey(
        @Query("apikey") apikey: String = API_KEY,
        @Query("q") query: String
    ): List<City>

    @GET("locations/v1/cities/geoposition/search")
    suspend fun getLocationKeyByLatLong(
        @Query("apikey") apikey: String = API_KEY,
        @Query("q") query: String
    ): List<City>

    @GET("forecasts/v1/daily/5day/{locationKey}")
    suspend fun getWeather(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apikey: String = API_KEY,
        @Query("metric") metric: Boolean
    ): Weather


}