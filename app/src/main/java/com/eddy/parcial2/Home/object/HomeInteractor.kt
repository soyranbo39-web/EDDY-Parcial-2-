package com.eddy.parcial2.home.`object`

import com.eddy.parcial2.Home.model.HomeUserData
import com.eddy.parcial2.data.interfaces.IUserRepository
import com.eddy.parcial2.Home.interfaces.IHomeInteractor

class HomeInteractor(private val userRepository: IUserRepository) : IHomeInteractor {
    override suspend fun getUserData(email: String): HomeUserData? {
        val user = userRepository.getUserByEmail(email)
        return user?.let {
            HomeUserData(
                username = it.username,
                email = it.email,
                avatarId = it.avatarId
            )
        }
    }
}
