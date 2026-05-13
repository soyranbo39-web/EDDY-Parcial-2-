package com.eddy.parcial2

import androidx.room.*

@Entity(tableName="ingresos")
data class Ingresos(
    @PrimaryKey val id : Int,
    val categoria : String,
    val monto : Int,
    val fecha : String,

)
