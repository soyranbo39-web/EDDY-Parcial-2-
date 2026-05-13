package com.eddy.parcial2.data.interfaces
import com.eddy.parcial2.data.models.User

interface IUserRepository {
    suspend fun insertUser(user: User): Boolean
    suspend fun authenticate(email: String, password: String): Boolean
    suspend fun createDemoUserIfNeeded()
    suspend fun getUserByEmail(email: String): User?
    suspend fun userExists(email: String): Boolean
}
