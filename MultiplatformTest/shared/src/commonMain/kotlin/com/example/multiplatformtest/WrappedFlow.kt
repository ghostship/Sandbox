package com.example.multiplatformtest

import kotlinx.coroutines.flow.Flow

expect class WrappedFlow<T>(flow: Flow<T>): Flow<T>

fun <T> Flow<T>.toWrappedFlow() = WrappedFlow(this)