package com.eddy.parcial2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.eddy.parcial2.data.AppDatabase
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class Pantalla8 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla8)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val montoInput = findViewById<EditText>(R.id.inputMonto)
        val descripcionInput = findViewById<EditText>(R.id.inputDescripcion)

        val spinnerCuenta = findViewById<Spinner>(R.id.spinnerSeleccionarCuenta)
        val spinnerCategoria = findViewById<Spinner>(R.id.spinnerCategoria)

        val botonFechaHora = findViewById<Button>(R.id.botonFechaHora)

        val cuentas = listOf(
            "Efectivo",
            "TarjetaDebito",
            "TarjetaCredito"
        )

        val categorias = listOf(
            "Comida",
            "Servicios",
            "Transporte",
            "Suscripciones"
        )

        spinnerCuenta.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            cuentas
        )

        spinnerCategoria.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categorias
        )

        val movimientoId = intent.getIntExtra("movimiento_id", -1)

        val db = AppDatabase.getDatabase(this)

        if (movimientoId != -1) {

            lifecycleScope.launch {

                val movimiento = db.movimientoDao().obtenerPorId(movimientoId)

                val formatted = NumberFormat
                    .getCurrencyInstance(Locale("es", "MX"))
                    .format(movimiento.cantidad)

                montoInput.setText(formatted)

                descripcionInput.setText(movimiento.descripcion)

                val cuentaIndex = cuentas.indexOf(movimiento.tipoCuenta)

                if (cuentaIndex >= 0) {
                    spinnerCuenta.setSelection(cuentaIndex)
                }

                val categoriaIndex = categorias.indexOf(movimiento.categoria)

                if (categoriaIndex >= 0) {
                    spinnerCategoria.setSelection(categoriaIndex)
                }

                botonFechaHora.text =
                    "${movimiento.dia}/${movimiento.mes}/${movimiento.ano}"
            }
        }

        montoInput.addTextChangedListener(object : TextWatcher {

            private var current = ""

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {}

            override fun afterTextChanged(s: Editable?) {

                if (s.toString() != current) {

                    montoInput.removeTextChangedListener(this)

                    val cleanString = s.toString()
                        .replace("$", "")
                        .replace(",", "")
                        .replace(".", "")

                    if (cleanString.isNotEmpty()) {

                        val parsed = cleanString.toDouble() / 100

                        val formatted = NumberFormat
                            .getCurrencyInstance(Locale("es", "MX"))
                            .format(parsed)

                        current = formatted

                        montoInput.setText(formatted)
                        montoInput.setSelection(formatted.length)
                    }

                    montoInput.addTextChangedListener(this)
                }
            }
        })
    }
}