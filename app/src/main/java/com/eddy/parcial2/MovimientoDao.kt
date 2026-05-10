package com.eddy.parcial2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovimientoDao {

    @Insert
    suspend fun insertar(movimiento: MovimientoContenedor)

    @Query("SELECT * FROM movimientos")
    suspend fun obtenerTodos(): List<MovimientoContenedor>
}