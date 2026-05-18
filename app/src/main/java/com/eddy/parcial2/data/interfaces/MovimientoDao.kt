package com.eddy.parcial2.data.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.eddy.parcial2.models.CategoriaTotal
import com.eddy.parcial2.models.Movimiento

@Dao
interface MovimientoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(movimiento: Movimiento)

    @Update
    suspend fun actualizar(movimiento: Movimiento)

    @Query("SELECT * FROM movimientos")
    suspend fun getMovimientos(): List<Movimiento>

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
    ): List<Movimiento>

    @Query("SELECT * FROM movimientos WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Movimiento

    @Query("SELECT * FROM movimientos WHERE categoria = :categoria")
    suspend fun getMovimientosPorCategoria(categoria: String): List<Movimiento>

    @Query("SELECT COALESCE(SUM(cantidad), 0.0) FROM movimientos WHERE tipoTransferencia = 'Ingreso'")
    suspend fun getTotalIngresos(): Double?

    @Query("""
        SELECT COALESCE(SUM(cantidad), 0.0) FROM movimientos
        WHERE tipoTransferencia = 'Ingreso'
        AND (:ano = -1 OR ano = :ano)
        AND (:mes = -1 OR mes = :mes)
    """)
    suspend fun getTotalIngresosFiltrado(ano: Int, mes: Int): Double?

    @Query("""
        SELECT categoria, SUM(cantidad) as total, COUNT(*) as movimientos
        FROM movimientos
        WHERE tipoTransferencia = 'Gasto'
        GROUP BY categoria
    """)
    suspend fun getGastosPorCategoria(): List<CategoriaTotal>

    @Query("""
        SELECT categoria, SUM(cantidad) as total, COUNT(*) as movimientos
        FROM movimientos
        WHERE tipoTransferencia = 'Gasto'
        AND (:ano = -1 OR ano = :ano)
        AND (:mes = -1 OR mes = :mes)
        GROUP BY categoria
    """)
    suspend fun getGastosPorCategoriaFiltrado(ano: Int, mes: Int): List<CategoriaTotal>

    @Query("DELETE FROM movimientos WHERE id = :id")
    suspend fun eliminarPorId(id: Int)

    @Query("SELECT DISTINCT categoria FROM movimientos ORDER BY categoria")
    suspend fun getCategorias(): List<String>
}
