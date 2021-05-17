package com.codeprecious.weatherapp

sealed class DataState<out T> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Loading<out T>(val data: T) : DataState<T>()
    data class Error(val message: String) : DataState<Nothing>()
    object Empty : DataState<Nothing>()
}
