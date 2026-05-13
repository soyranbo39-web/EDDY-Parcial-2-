package com.eddy.parcial2

import androidx.room.*

@Dao
interface ingresosDao {
    @Transaction
    @Query("SELECT * FROM ingresos")
    fun getIngresos(): List<Ingresos>

    @Transaction
    @Query("SELECT monto FROM ingresos")
    fun getIngresosMontos(): List<Int>
}