package com.example.multiplatformtest

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform