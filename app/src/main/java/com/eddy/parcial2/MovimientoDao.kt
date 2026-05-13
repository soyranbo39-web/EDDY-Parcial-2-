package com.eddy.parcial2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MovimientoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(movimiento: MovimientoContenedor)

    @Update
    suspend fun actualizar(movimiento: MovimientoContenedor)

    @Query("SELECT * FROM movimientos")
    suspend fun getMovimientos(): List<MovimientoContenedor>

    @Query("SELECT DISTINCT nombreCuenta FROM movimientos ORDER BY nombreCuenta")
    suspend fun obtenerCuentas(): List<String>

    @Query("SELECT DISTINCT ano FROM movimientos ORDER BY ano")
    suspend fun obtenerAnos(): List<Int>

    @Query("SELECT DISTINCT mes FROM movimientos ORDER BY mes")
    suspend fun obtenerMeses(): List<Int>

    @Query("""
        SELECT * FROM movimientos
        WHERE (:cuenta = 'Cuenta' OR nombreCuenta = :cuenta)
        AND (:ano = -1 OR ano = :ano)
        AND (:mes = -1 OR mes = :mes)
        ORDER BY
        CASE WHEN :modo = 'cuenta' THEN nombreCuenta END ASC,
        CASE WHEN :modo = 'cantidad' THEN cantidad END DESC,
        ano DESC, mes DESC, dia DESC
    """)
    suspend fun obtenerFiltrados(
        cuenta: String,
        ano: Int,
        mes: Int,
        modo: String
    ): List<MovimientoContenedor>

    @Query("SELECT * FROM movimientos WHERE id = :id")
    suspend fun obtenerPorId(id: Int): MovimientoContenedor
}