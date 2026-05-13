package com.eddy.parcial2.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="gastos")
data class Gastos(
    @PrimaryKey val id : Int,
    val categoria : String,
    val monto : Int,
    val fecha : String
)