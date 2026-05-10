package com.eddy.parcial2

data class MovimientoContenedor(
    val ano: Int,
    val mes: Int,
    val dia: Int,
    val cantidad: Double,
    val descripcion: String,
    val nombreCuenta: String,
    val tipoCuenta: TipoCuenta,
    val tipoTransferencia: TipoTransferencia,
    val categoria: String
)

enum class TipoCuenta{
    Efectivo,
    TarjetaDebito,
    TarjetaCredito
}

enum class TipoTransferencia{
    Ingreso,
    Gasto,
    Transferencia
}