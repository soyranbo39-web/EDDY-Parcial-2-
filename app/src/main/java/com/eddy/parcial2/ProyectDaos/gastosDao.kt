package com.eddy.parcial2.ProyectDaos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eddy.parcial2.models.Gastos
import com.eddy.parcial2.models.CategoriaTotal

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

    @Query("SELECT categoria, SUM(monto) as total FROM gastos GROUP BY categoria")
    fun getGastosPorCategoria(): List<CategoriaTotal>
}