package com.eddy.parcial2.registrar.model

data class RegisterData(
    val email: String,
    val username: String,
    val password: String,
    val confirmPassword: String,
    val avatarId: Int
)
