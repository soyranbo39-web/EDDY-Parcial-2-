package com.eddy.parcial2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eddy.parcial2.data.AppDatabase
import kotlinx.coroutines.launch

class Pantalla7 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla7)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerListaMovimientos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            db.movimientoDao().insertar(
                MovimientoContenedor(
                    ano = 2026,
                    mes = 1,
                    dia = 10,
                    cantidad = 15.0,
                    descripcion = "Netflix",
                    nombreCuenta = "Cuenta A",
                    tipoCuenta = "TarjetaCredito",
                    tipoTransferencia = "Transferencia",
                    categoria = "Suscripciones"
                )
            )

            db.movimientoDao().insertar(
                MovimientoContenedor(
                    ano = 2026,
                    mes = 1,
                    dia = 11,
                    cantidad = 42.0,
                    descripcion = "Groceries",
                    nombreCuenta = "Cuenta B",
                    tipoCuenta = "Efectivo",
                    tipoTransferencia = "Gasto",
                    categoria = "Comida"
                )
            )

            db.movimientoDao().insertar(
                MovimientoContenedor(
                    ano = 2026,
                    mes = 1,
                    dia = 12,
                    cantidad = 80.0,
                    descripcion = "Electricity",
                    nombreCuenta = "Cuenta A",
                    tipoCuenta = "TarjetaDebito",
                    tipoTransferencia = "Ingreso",
                    categoria = "Servicios"
                )
            )

            db.movimientoDao().insertar(
                MovimientoContenedor(
                    ano = 2026,
                    mes = 1,
                    dia = 13,
                    cantidad = 20.0,
                    descripcion = "Gas",
                    nombreCuenta = "Cuenta C",
                    tipoCuenta = "Efectivo",
                    tipoTransferencia = "Gasto",
                    categoria = "Transporte"
                )
            )

            db.movimientoDao().insertar(
                MovimientoContenedor(
                    ano = 2026,
                    mes = 1,
                    dia = 14,
                    cantidad = 60.0,
                    descripcion = "Internet",
                    nombreCuenta = "Cuenta A",
                    tipoCuenta = "TarjetaCredito",
                    tipoTransferencia = "Gasto",
                    categoria = "Servicios"
                )
            )

            db.movimientoDao().insertar(
                MovimientoContenedor(
                    ano = 2026,
                    mes = 2,
                    dia = 15,
                    cantidad = 60.0,
                    descripcion = "Trabajo",
                    nombreCuenta = "Cuenta A",
                    tipoCuenta = "TarjetaCredito",
                    tipoTransferencia = "Ingreso",
                    categoria = "Servicios"
                )
            )

            val lista = db.movimientoDao().obtenerTodos()
            recyclerView.adapter = MovimientoAdapter(lista)
        }
    }
}