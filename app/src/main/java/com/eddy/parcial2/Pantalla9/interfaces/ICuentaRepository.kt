package com.eddy.parcial2.Pantalla9.interfaces

import com.eddy.parcial2.Pantalla9.models.Cuenta

interface ICuentaRepository {

    suspend fun obtenerCuentas(): List<Cuenta>

    suspend fun insertar(cuenta: Cuenta)

    suspend fun actualizar(cuenta: Cuenta)

    suspend fun eliminar(cuenta: Cuenta)

    suspend fun tieneMovimientos(nombreCuenta: String): Boolean

    suspend fun obtenerPorId(id: Int): Cuenta?
}
