package com.eddy.parcial2.ProyectDaos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eddy.parcial2.models.Gastos

@Dao
interface gastosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGasto(gasto: Gastos)

    @Query("SELECT * FROM gastos")
    fun getGastos(): List<Gastos>

    @Query("SELECT * FROM gastos WHERE categoria = :categoria")
    fun getGastosCategoria(categoria: String): List<Gastos>

    @Query("SELECT monto FROM gastos")
    fun getMontos(): List<Int>
}