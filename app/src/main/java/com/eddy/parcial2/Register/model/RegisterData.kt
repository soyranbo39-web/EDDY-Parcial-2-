package com.eddy.parcial2.Register.model

data class RegisterData(
    val email: String,
    val username: String,
    val password: String,
    val confirmPassword: String,
    val avatarId: Int
)
