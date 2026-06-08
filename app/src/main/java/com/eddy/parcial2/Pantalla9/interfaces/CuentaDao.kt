package com.eddy.parcial2.Pantalla9.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.eddy.parcial2.Pantalla9.models.Cuenta

@Dao
interface CuentaDao {

    @Query("SELECT * FROM cuentas ORDER BY descripcion DESC")
    suspend fun obtenerCuentasDesc(): List<Cuenta>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(cuenta: Cuenta)

    @Update
    suspend fun actualizar(cuenta: Cuenta)

    @Delete
    suspend fun eliminar(cuenta: Cuenta)

    @Query("SELECT COUNT(*) FROM movimientos WHERE nombreCuenta = :nombreCuenta")
    suspend fun contarMovimientosPorCuenta(nombreCuenta: String): Int

    @Query("SELECT * FROM cuentas WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Cuenta?
}
