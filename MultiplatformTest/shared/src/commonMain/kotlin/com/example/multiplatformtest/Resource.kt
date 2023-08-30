package com.example.multiplatformtest

sealed class Resource<T : Any> {

    data class Loading<T : Any>(val partialData: T? = null) : Resource<T>()

    data class Success<T : Any>(val data: T) : Resource<T>()

    data class Error<T : Any>(val exception: Exception) : Resource<T>()
}