package com.example.multiplatformtest

interface SampleRepository {
    /**
     * Get a list of users
     */
    fun getUsers(): WrappedFlow<Resource<List<User>>>

    /**
     * Get a single user by id
     */
    fun getUser(userId: Int): WrappedFlow<Resource<User>>

    /**
     * Add a new user
     */
    fun createUser(newUser: User): WrappedFlow<Resource<Unit>>

    /**
     * Update existing users matching [newUser].id
     */
    fun updateUser(newUser: User): WrappedFlow<Resource<Unit>>

    /**
     * Remove any user matching [userId]
     */
    fun deleteUser(userId: Int): WrappedFlow<Resource<Unit>>
}
