package com.eddy.parcial2.Login.`object`

import android.util.Patterns
import com.eddy.parcial2.data.interfaces.IUserRepository


import com.eddy.parcial2.data.models.User
import com.eddy.parcial2.Login.interfaces.ILoginInteractor
import com.eddy.parcial2.Login.model.LoginCredentials
import com.eddy.parcial2.Login.model.LoginResult

class LoginInteractor(private val userRepository: IUserRepository) : ILoginInteractor {

    override fun validate(credentials: LoginCredentials): String? {
        return when {
            credentials.email.isEmpty() -> "El correo es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(credentials.email).matches() -> "Correo inválido"
            credentials.password.isEmpty() -> "La contraseña es obligatoria"
            credentials.password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }

    override suspend fun login(credentials: LoginCredentials): LoginResult {
        val success = userRepository.authenticate(credentials.email, credentials.password)
        return if (success) LoginResult.Success else LoginResult.Error("Correo o contraseña incorrectos")
    }

    override suspend fun register(credentials: LoginCredentials): LoginResult {
        val newUser = User(
            email = credentials.email,
            username = credentials.email.substringBefore("@"),
            passwordHash = credentials.password,
            avatarId = 1
        )
        val success = userRepository.insertUser(newUser)
        return if (success) LoginResult.Success else LoginResult.Error("Ese correo ya existe")
    }
}
