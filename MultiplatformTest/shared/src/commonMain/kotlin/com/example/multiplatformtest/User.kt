package com.example.multiplatformtest

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String
) {
    val fullName = "$firstName $lastName"
}
