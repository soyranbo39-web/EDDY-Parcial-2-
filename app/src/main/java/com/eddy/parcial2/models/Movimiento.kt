package com.eddy.parcial2.models

data class Movimiento(

    val iconoCuenta: Int,

    val cuenta: String,

    val fecha: String,

    val descripcion: String,

    val cantidad: Double
)