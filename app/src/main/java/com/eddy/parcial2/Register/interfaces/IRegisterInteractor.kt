package com.eddy.parcial2.Register.interfaces

import com.eddy.parcial2.Register.model.RegisterData
import com.eddy.parcial2.Login.model.LoginResult

interface IRegisterInteractor {
    fun validate(data: RegisterData): String?
    suspend fun register(data: RegisterData): LoginResult
}
