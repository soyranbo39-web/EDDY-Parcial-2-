package com.eddy.parcial2.Pantalla9.repository

import com.eddy.parcial2.Pantalla9.interfaces.CuentaDao
import com.eddy.parcial2.Pantalla9.interfaces.ICuentaRepository
import com.eddy.parcial2.Pantalla9.models.Cuenta

class CuentaRepository(private val cuentaDao: CuentaDao) : ICuentaRepository {

    override suspend fun obtenerCuentas(): List<Cuenta> =
        cuentaDao.obtenerCuentasDesc()

    override suspend fun insertar(cuenta: Cuenta) =
        cuentaDao.insertar(cuenta)

    override suspend fun actualizar(cuenta: Cuenta) =
        cuentaDao.actualizar(cuenta)

    override suspend fun eliminar(cuenta: Cuenta) =
        cuentaDao.eliminar(cuenta)

    override suspend fun tieneMovimientos(nombreCuenta: String): Boolean =
        cuentaDao.contarMovimientosPorCuenta(nombreCuenta) > 0

    override suspend fun obtenerPorId(id: Int): Cuenta? =
        cuentaDao.obtenerPorId(id)
}
