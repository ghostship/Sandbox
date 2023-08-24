package com.example.multiplatformtest

import kotlinx.coroutines.flow.Flow

actual class WrappedFlow<T> actual constructor(flow: Flow<T>) : Flow<T> by flow