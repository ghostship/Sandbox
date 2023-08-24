package com.example.multiplatformtest

sealed class Resource<T> {

    data class Loading<T>(val partialData: T? = null) : Resource<T>()

    data class Success<T>(val data: T) : Resource<T>()

    data class Error<T>(val exception: Exception) : Resource<T>()
}