package com.eddy.parcial2
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "movimientos")
data class MovimientoContenedor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val ano: Int,
    val mes: Int,
    val dia: Int,
    val cantidad: Double,
    val descripcion: String,
    val nombreCuenta: String,
    val tipoCuenta: String,
    val tipoTransferencia: String,
    val categoria: String
)

/*
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
 */