package com.eddy.parcial2.login.interfaces

import com.eddy.parcial2.login.model.LoginCredentials
import com.eddy.parcial2.login.model.LoginResult

interface ILoginInteractor {
    fun validate(credentials: LoginCredentials): String?
    suspend fun login(credentials: LoginCredentials): LoginResult
    suspend fun register(credentials: LoginCredentials): LoginResult
}
