package com.eddy.parcial2.FinanzasCompartidas.models

data class GastoCompartido(
    val id: String = "",
    val descripcion: String = "",
    val monto: Double = 0.0,
    val pagadoPorUid: String = "",
    val pagadoPorNombre: String = "",
    val timestamp: Long = 0L
)
