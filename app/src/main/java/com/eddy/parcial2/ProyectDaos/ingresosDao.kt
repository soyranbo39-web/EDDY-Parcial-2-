package com.eddy.parcial2.ProyectDaos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.eddy.parcial2.models.Ingresos

@Dao
interface ingresosDao {
    @Transaction
    @Query("SELECT * FROM ingresos")
    fun getIngresos(): List<Ingresos>

    @Transaction
    @Query("SELECT monto FROM ingresos")
    fun getIngresosMontos(): List<Int>
}