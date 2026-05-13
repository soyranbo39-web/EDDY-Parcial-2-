package com.eddy.parcial2.ProyectDaos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.eddy.parcial2.models.Categoria
import com.eddy.parcial2.models.Gastos

@Dao
interface gastosDao {
    @Transaction
    @Query("SELECT * FROM gastos")
    fun getGastos(): List<Gastos>


    @Transaction
    @Query("SELECT monto FROM gastos")
    fun getGastosMonto(): List<Gastos>

    @Transaction
    @Query("SELECT * FROM gastos WHERE categoria = :categoria")
    fun getGastosCategoria(categoria: Categoria): List<Gastos>

    @Transaction
    @Query("SELECT monto FROM gastos")
    fun getMontos(): List<Int>
}