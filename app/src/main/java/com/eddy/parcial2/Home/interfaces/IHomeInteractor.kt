package com.eddy.parcial2.Home.interfaces

import com.eddy.parcial2.Home.model.HomeUserData

interface IHomeInteractor {
    suspend fun getUserData(email: String): HomeUserData?
}
