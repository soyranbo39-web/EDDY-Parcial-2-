package com.eddy.parcial2.utils

import com.eddy.parcial2.R
import com.eddy.parcial2.models.CategoriaResumen
import com.eddy.parcial2.models.Movimiento

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

    fun obtenerMovimientos(): MutableList<Movimiento> {

        return mutableListOf(

//            Movimiento(
//                R.drawable.ic_wallet,
//                "Efectivo",
//                "09/05/2026",
//                "Hamburguesas",
//                250.0
//            ),
//
//            Movimiento(
//                R.drawable.ic_card,
//                "T. Débito",
//                "08/05/2026",
//                "Supermercado",
//                1200.0
//            ),
//
//            Movimiento(
//                R.drawable.ic_wallet,
//                "Efectivo",
//                "06/05/2026",
//                "Cena",
//                450.0
//            ),
//
//            Movimiento(
//                R.drawable.ic_card,
//                "T. Crédito",
//                "05/05/2026",
//                "Pizza",
//                320.0
//            )
        )
    }
}