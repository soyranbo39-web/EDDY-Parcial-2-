package com.eddy.parcial2.data.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.eddy.parcial2.models.Categoria

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM categorias ORDER BY descripcion DESC")
    suspend fun getCategoriasDesc(): List<Categoria>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(categoria: Categoria)

    @Update
    suspend fun actualizar(categoria: Categoria)

    @Delete
    suspend fun eliminar(categoria: Categoria)

    @Query("SELECT COUNT(*) FROM movimientos WHERE categoria = :nombreCategoria")
    suspend fun countMovimientosByCategoria(nombreCategoria: String): Int

    @Query("SELECT * FROM categorias WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Categoria?
}
