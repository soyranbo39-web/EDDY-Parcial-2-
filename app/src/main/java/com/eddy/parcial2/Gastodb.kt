package com.eddy.parcial2

import androidx.room.*

@Entity(tableName="gastos")
data class Gastodb(
    @PrimaryKey val id : Int,
    val categoria : String,
    val monto : Int,
    val fecha : String,
)
