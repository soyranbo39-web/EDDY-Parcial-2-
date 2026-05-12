package com.eddy.parcial2

import androidx.room.*

@Dao
interface MovimirentoDao {
    @Transaction
    @Query("SELECT * FROM movimientos")
    fun getMovimientos(): List<Movimiento>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovimeinto(post: Movimiento)
}