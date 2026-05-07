package com.eddy.parcial2
import androidx.room.*
import java.util.Date

@Entity(tableName="gastos")
data class Gastos(
    @PrimaryKey val id : Int,
    val categoria : String,
    val monto : Int,
    val fecha : Date
)
