package com.eddy.parcial2.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
// Entidad de usuario
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "password_hash")
    val passwordHash: String
)
