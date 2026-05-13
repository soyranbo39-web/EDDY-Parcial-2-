package com.eddy.parcial2.models

import androidx.room.*

@Entity(tableName = "movimientos")
data class Movimiento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tipoMovimiento: String,
    val cantidad: Int,
    val cuenta: String,
    val categoria: String,

    val cuentaOrigen: String,
    val cuentaDestino: String,

    val descripcion:String,
    val fecha: String,
)
