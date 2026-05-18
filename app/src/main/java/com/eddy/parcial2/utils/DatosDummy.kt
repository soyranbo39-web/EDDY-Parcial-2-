package com.eddy.parcial2.utils

import com.eddy.parcial2.R
import com.eddy.parcial2.models.CategoriaResumen

object DatosDummy {
    fun obtenerCategorias(): MutableList<CategoriaResumen> {
        return mutableListOf(
            CategoriaResumen(
                R.drawable.ic_food,
                "Comida",
                12,
                5400.0,
                40
            ),
            CategoriaResumen(
                R.drawable.ic_house,
                "Casa",
                5,
                3200.0,
                25
            ),
            CategoriaResumen(
                R.drawable.ic_clothes,
                "Ropa",
                8,
                2600.0,
                20
            ),
            CategoriaResumen(
                R.drawable.ic_gas,
                "Gasolina",
                4,
                1800.0,
                15
            )
        )
    }
}
