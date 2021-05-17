package com.codeprecious.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeprecious.weatherapp.model.Weather
import com.codeprecious.weatherapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _weather: MutableStateFlow<StateEvent<Weather>> =
        MutableStateFlow(StateEvent.Empty)

    private var city: String = "Cairo"
    private var switchBar: Boolean = false

    fun getCity(): String {
        return city
    }

    fun getSwitchBar(): Boolean {
        return switchBar
    }

    fun setSwitchBar(check: Boolean) {
        switchBar = check
    }

    val weatherStateFlow: StateFlow<StateEvent<Weather>>
        get() = _weather


    fun getLocationKey(query: String) =
        viewModelScope.launch {
            repository.getLocationKey(query).collect {
                _weather.value = StateEvent.Loading

                try {
                    getWeather(it[0].Key, switchBar)

                } catch (e: Exception) {
                    _weather.value = StateEvent.Error(e.message!!)
                }
            }
            city = query
        }

    fun getLocationKeyByLatLong(query: String) =
        viewModelScope.launch {
            repository.getLocationKeyByLatLong(query).collect {
                _weather.value = StateEvent.Loading

                try {
                    city = it[0].EnglishName
                    getWeather(it[0].Key, switchBar)
                } catch (e: Exception) {
                    _weather.value = StateEvent.Error(e.message!!)
                }
            }
        }

    private fun getWeather(key: String, metric: Boolean) =
        viewModelScope.launch {
            try {
                repository.getWeather(key = key, metric = metric).collect {
                    _weather.value = StateEvent.Success(it)
                }
            } catch (e: Exception) {
                _weather.value = StateEvent.Error(e.message!!)
            }
        }

    sealed class StateEvent<out T> {
        data class Success<out T>(val data: T) : StateEvent<T>()
        object Loading : StateEvent<Nothing>()
        data class Error(val message: String) : StateEvent<Nothing>()
        object Empty : StateEvent<Nothing>()
    }
}