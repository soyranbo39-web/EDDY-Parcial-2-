package com.eddy.parcial2.Register.objec

import android.util.Patterns
import com.eddy.parcial2.Register.interfaces.IRegisterInteractor
import com.eddy.parcial2.Register.model.RegisterData

import com.eddy.parcial2.data.models.User
import com.eddy.parcial2.Login.model.LoginResult
import java.security.MessageDigest
import com.eddy.parcial2.data.interfaces.IUserRepository

class RegisterInteractor(private val userRepository: IUserRepository) : IRegisterInteractor {

    override fun validate(data: RegisterData): String? {
        return when {
            data.email.isEmpty() -> "El correo es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(data.email).matches() -> "Correo inválido"
            data.username.isEmpty() -> "El nombre de usuario es obligatorio"
            data.password.isEmpty() -> "La contraseña es obligatoria"
            data.password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            data.password != data.confirmPassword -> "Las contraseñas no coinciden"
            data.avatarId == 0 -> "Debes seleccionar un avatar"
            else -> null
        }
    }

    override suspend fun register(data: RegisterData): LoginResult {
        if (userRepository.userExists(data.email)) {
            return LoginResult.Error("Ese correo ya existe")
        }

        val newUser = User(
            email = data.email,
            username = data.username,
            passwordHash = hashPassword(data.password),
            avatarId = data.avatarId
        )

        val success = userRepository.insertUser(newUser)
        return if (success) LoginResult.Success else LoginResult.Error("Error al registrar usuario")
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}
