package com.eddy.parcial2.data

import com.eddy.parcial2.data.Interfaces.UserDao
import com.eddy.parcial2.data.models.User
import java.security.MessageDigest

class UserRepository(private val userDao: UserDao) {
    // Función para crear un usuario
    suspend fun insertUser(email: String, password: String): Boolean {
        val user = User(
            email = email.lowercase(),
            passwordHash = hashPassword(password)
        )
        return userDao.insertUser(user) != -1L
    }
    // Función para autenticar un usuario
    suspend fun authenticate(email: String, password: String): Boolean {
        val user = userDao.getUserByEmail(email.lowercase())
        return user != null && user.passwordHash == hashPassword(password)
    }

 // Crear un usuario demo en la base de datos
    suspend fun createDemoUserIfNeeded() {
        val demoEmail = "demo@correo.com"
        val demoPassword = "123456"
        if (!userDao.userExists(demoEmail)) {
            insertUser(demoEmail, demoPassword)
        }
    }
 // Función para hashear la contraseña UTILIZANDO SHA-256
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}
