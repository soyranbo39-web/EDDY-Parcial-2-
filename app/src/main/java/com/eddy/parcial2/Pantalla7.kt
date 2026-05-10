package com.eddy.parcial2

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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

        val recyclerView =
            findViewById<RecyclerView>(R.id.recyclerListaMovimientos)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val spinnerCuenta =
            findViewById<Spinner>(R.id.spinnerSortCuenta)

        val spinnerAno =
            findViewById<Spinner>(R.id.spinnerSortAno)

        val spinnerMes =
            findViewById<Spinner>(R.id.spinnerSortMes)

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

            val cuentas = mutableListOf("Cuenta")
            cuentas.addAll(db.movimientoDao().obtenerCuentas())

            val anos = mutableListOf(-1)
            anos.addAll(db.movimientoDao().obtenerAnos())

            val meses = mutableListOf(-1)
            meses.addAll(db.movimientoDao().obtenerMeses())

            val anosTexto = anos.map {
                if (it == -1) "Año" else it.toString()
            }

            val mesesTexto = meses.map {
                if (it == -1) "Mes" else it.toString()
            }

            spinnerCuenta.adapter = ArrayAdapter(
                this@Pantalla7,
                android.R.layout.simple_spinner_dropdown_item,
                cuentas
            )

            spinnerAno.adapter = ArrayAdapter(
                this@Pantalla7,
                android.R.layout.simple_spinner_dropdown_item,
                anosTexto
            )

            spinnerMes.adapter = ArrayAdapter(
                this@Pantalla7,
                android.R.layout.simple_spinner_dropdown_item,
                mesesTexto
            )

            fun actualizarLista() {

                val cuentaSeleccionada =
                    spinnerCuenta.selectedItem.toString()

                val anoSeleccionado =
                    anos[spinnerAno.selectedItemPosition]

                val mesSeleccionado =
                    meses[spinnerMes.selectedItemPosition]

                lifecycleScope.launch {

                    val filtrados =
                        db.movimientoDao().obtenerFiltrados(
                            cuentaSeleccionada,
                            anoSeleccionado,
                            mesSeleccionado
                        )

                    recyclerView.adapter =
                        MovimientoAdapter(filtrados)
                }
            }

            val listener =
                object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        actualizarLista()
                    }

                    override fun onNothingSelected(
                        parent: AdapterView<*>?
                    ) {
                    }
                }

            spinnerCuenta.onItemSelectedListener = listener
            spinnerAno.onItemSelectedListener = listener
            spinnerMes.onItemSelectedListener = listener

            actualizarLista()
        }
    }
}