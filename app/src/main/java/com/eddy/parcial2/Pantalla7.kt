package com.eddy.parcial2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Pantalla7 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla7)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerListaMovimientos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val lista = listOf(
            MovimientoContenedor(2026, 1, 10, 15.0, "Netflix", "Cuenta A", TipoCuenta.TarjetaCredito, TipoTransferencia.Transferencia, "Suscripciones"),
            MovimientoContenedor(2026, 1, 11, 42.0, "Groceries", "Cuenta B", TipoCuenta.Efectivo, TipoTransferencia.Gasto, "Comida"),
            MovimientoContenedor(2026, 1, 12, 80.0, "Electricity", "Cuenta A", TipoCuenta.TarjetaDebito, TipoTransferencia.Ingreso, "Servicios"),
            MovimientoContenedor(2026, 1, 13, 20.0, "Gas", "Cuenta C", TipoCuenta.Efectivo, TipoTransferencia.Gasto, "Transporte"),
            MovimientoContenedor(2026, 1, 14, 60.0, "Internet", "Cuenta A", TipoCuenta.TarjetaCredito, TipoTransferencia.Gasto, "Servicios"),
            MovimientoContenedor(2026, 2, 15, 60.0, "Trabajo", "Cuenta A", TipoCuenta.TarjetaCredito, TipoTransferencia.Ingreso, "Servicios")

        )

        recyclerView.adapter = MovimientoAdapter(lista)
    }
}