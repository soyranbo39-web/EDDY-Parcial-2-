package com.eddy.parcial2.Login.interfaces

import com.eddy.parcial2.Login.model.LoginCredentials
import com.eddy.parcial2.Login.model.LoginResult

interface ILoginInteractor {
    fun validate(credentials: LoginCredentials): String?
    suspend fun login(credentials: LoginCredentials): LoginResult
    suspend fun register(credentials: LoginCredentials): LoginResult
}
