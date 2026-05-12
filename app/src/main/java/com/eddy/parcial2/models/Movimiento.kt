package com.eddy.parcial2.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movimiento(

    val iconoCuenta: Int,

    val cuenta: String,

    val fecha: String,

    val descripcion: String,

    val cantidad: Double

) : Parcelable