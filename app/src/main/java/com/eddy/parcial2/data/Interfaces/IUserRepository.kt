package com.eddy.parcial2.data.Interfaces

import com.eddy.parcial2.data.models.User

interface IUserRepository {
    suspend fun insertUser(email: String, password: String): Boolean
    suspend fun authenticate(email: String, password: String): Boolean
    suspend fun createDemoUserIfNeeded()
    suspend fun getUserByEmail(email: String): User?
}
