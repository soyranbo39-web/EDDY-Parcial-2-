package com.eddy.parcial2.home.interfaces

import com.eddy.parcial2.home.model.HomeUserData

interface IHomeInteractor {
    suspend fun getUserData(email: String): HomeUserData?
}
