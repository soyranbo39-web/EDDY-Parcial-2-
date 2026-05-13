package com.eddy.parcial2.ProyectDaos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.eddy.parcial2.Movimiento

@Dao
interface MovimientoDao {
    @Transaction
    @Query("SELECT * FROM movimientos")
    fun getMovimientos(): List<Movimiento>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMovimiento(post: Movimiento)
}