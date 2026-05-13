package com.eddy.parcial2.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="ingresos")
data class Ingresos(
    @PrimaryKey val id : Int,
    val categoria : String,
    val monto : Int,
    val fecha : String,

    )