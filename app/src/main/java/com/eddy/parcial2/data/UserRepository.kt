package com.eddy.parcial2.data
import com.eddy.parcial2.data.interfaces.IUserRepository
import com.eddy.parcial2.data.interfaces.UserDao
import com.eddy.parcial2.data.models.User
import java.security.MessageDigest

class UserRepository(private val userDao: UserDao) : IUserRepository {

    override suspend fun insertUser(user: User): Boolean {
        return userDao.insertUser(user) != -1L
    }

    override suspend fun authenticate(email: String, password: String): Boolean {
        val user = userDao.getUserByEmail(email.lowercase())
        return user != null && user.passwordHash == hashPassword(password)
    }

    override suspend fun createDemoUserIfNeeded() {
        val demoEmail = "demo@correo.com"
        if (!userDao.userExists(demoEmail)) {
            val demoUser = User(
                email = demoEmail,
                username = "Usuario Demo",
                passwordHash = hashPassword("123456"),
                avatarId = 1
            )
            insertUser(demoUser)
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email.lowercase())
    }

    override suspend fun userExists(email: String): Boolean {
        return userDao.userExists(email.lowercase())
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}
