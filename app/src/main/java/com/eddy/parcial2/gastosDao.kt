package com.eddy.parcial2
import androidx.room.*

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
}