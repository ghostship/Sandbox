package com.example.multiplatformtest

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class SampleRepositoryImpl : SampleRepository {
    private val usersCache = TEST_USERS.toMutableList()

    /**
     * Get a list of users
     */
    override fun getUsers(): WrappedFlow<Resource<List<User>>> = flow {
        emit(Resource.Loading())

        delay(RESPONSE_DELAY)

        emit(Resource.Success(usersCache.toList()))

    }.toWrappedFlow()

    /**
     * Get a single user by id
     */
    override fun getUser(userId: Int): WrappedFlow<Resource<User>> = flow {
        emit(Resource.Loading())

        delay(RESPONSE_DELAY)

        val user = usersCache.find { it.id == userId }

        if (user != null) {
            emit(Resource.Success(user))
        } else {
            emit(Resource.Error(Exception("User not found")))
        }
    }.toWrappedFlow()

    /**
     * Add a new user
     */
    override fun createUser(newUser: User): WrappedFlow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        delay(RESPONSE_DELAY)

        usersCache.add(newUser)

        emit(Resource.Success(Unit))
    }.toWrappedFlow()

    /**
     * Update existing users matching [newUser].id
     */
    override fun updateUser(newUser: User): WrappedFlow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        delay(RESPONSE_DELAY)

        var isUserUpdated = false

        usersCache.forEachIndexed { index, user ->
            if (user.id == newUser.id) {
                usersCache[index] = newUser
                isUserUpdated = true
            }
        }

        if (isUserUpdated) {
            emit(Resource.Success(Unit))
        } else {
            emit(Resource.Error(Exception("User not found")))
        }
    }.toWrappedFlow()

    /**
     * Remove any user matching [userId]
     */
    override fun deleteUser(userId: Int): WrappedFlow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        delay(RESPONSE_DELAY)

        usersCache.removeAll { it.id == userId }

        emit(Resource.Success(Unit))
    }.toWrappedFlow()

    companion object {
        private val RESPONSE_DELAY = 1000L

        private val TEST_USERS = listOf(
            User(1, "Tom", "Userman"),
            User(2, "Sally", "Lastaname"),
            User(3, "James", "Smith"),
            User(4, "Amy", "Anyman"),
            User(5, "Frank", "McPerson"),
            User(6, "Jessica", "VonUserson"),
            User(7, "Sam", "Eman")
        )
    }
}