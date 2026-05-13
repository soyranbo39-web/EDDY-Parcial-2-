package com.eddy.parcial2.registrar.interfaces

import com.eddy.parcial2.registrar.model.RegisterData
import com.eddy.parcial2.Login.model.LoginResult

interface IRegisterInteractor {
    fun validate(data: RegisterData): String?
    suspend fun register(data: RegisterData): LoginResult
}
