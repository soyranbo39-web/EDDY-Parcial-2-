package com.eddy.parcial2

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.eddy.parcial2.activities.Activity3PantallaDeInicio
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.models.Movimiento
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class Pantalla8 : AppCompatActivity() {

    private var movimientoActual: Movimiento? = null

    private var selectedDia = 0
    private var selectedMes = 0
    private var selectedAno = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla8)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val montoInput       = findViewById<EditText>(R.id.inputMonto)
        val descripcionInput = findViewById<EditText>(R.id.inputDescripcion)
        val spinnerCuenta    = findViewById<Spinner>(R.id.spinnerSeleccionarCuenta)
        val spinnerCategoria = findViewById<Spinner>(R.id.spinnerCategoria)
        val botonFechaHora   = findViewById<Button>(R.id.botonFechaHora)
        val botonGuardar     = findViewById<Button>(R.id.botonGuardar)
        val botonBack        = findViewById<View>(R.id.botonTopRegresar)

        botonBack.setOnClickListener { goHome() }

        val cuentas   = listOf("Efectivo", "TarjetaDebito", "TarjetaCredito")
        val categorias = listOf("Comida", "Servicios", "Transporte", "Suscripciones",
                                "Casa", "Ropa", "Gasolina", "Despensa")

        spinnerCuenta.adapter    = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cuentas)
        spinnerCategoria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)

        val movimientoId = intent.getIntExtra("movimiento_id", -1)
        val db = AppDatabase.getDatabase(this)

        if (movimientoId != -1) {
            lifecycleScope.launch {
                val movimiento = db.movimientoDao().obtenerPorId(movimientoId)
                movimientoActual = movimiento

                val formatted = NumberFormat
                    .getCurrencyInstance(Locale("es", "MX"))
                    .format(movimiento.cantidad)

                montoInput.setText(formatted)
                descripcionInput.setText(movimiento.descripcion)

                val cuentaIndex = cuentas.indexOf(movimiento.tipoCuenta)
                if (cuentaIndex >= 0) spinnerCuenta.setSelection(cuentaIndex)

                val categoriaIndex = categorias.indexOf(movimiento.categoria)
                if (categoriaIndex >= 0) spinnerCategoria.setSelection(categoriaIndex)

                selectedDia = movimiento.dia
                selectedMes = movimiento.mes
                selectedAno = movimiento.ano

                botonFechaHora.text = "$selectedDia/$selectedMes/$selectedAno"
            }
        }

        botonFechaHora.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year  = if (selectedAno != 0) selectedAno else calendar.get(Calendar.YEAR)
            val month = if (selectedMes != 0) selectedMes - 1 else calendar.get(Calendar.MONTH)
            val day   = if (selectedDia != 0) selectedDia else calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, y, m, d ->
                selectedAno = y; selectedMes = m + 1; selectedDia = d
                botonFechaHora.text = "$selectedDia/$selectedMes/$selectedAno"
            }, year, month, day).show()
        }

        botonGuardar.setOnClickListener {
            val actual = movimientoActual ?: return@setOnClickListener
            val clean  = montoInput.text.toString().replace("$", "").replace(",", "").trim()
            val cantidad = clean.toDoubleOrNull() ?: actual.cantidad

            val updated = actual.copy(
                cantidad     = cantidad,
                descripcion  = descripcionInput.text.toString(),
                tipoCuenta   = spinnerCuenta.selectedItem.toString(),
                nombreCuenta = spinnerCuenta.selectedItem.toString(),
                categoria    = spinnerCategoria.selectedItem.toString(),
                dia          = selectedDia,
                mes          = selectedMes,
                ano          = selectedAno
            )
            lifecycleScope.launch {
                db.movimientoDao().actualizar(updated)
                finish()
            }
        }

        montoInput.addTextChangedListener(object : TextWatcher {
            private var current = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    montoInput.removeTextChangedListener(this)
                    val cleanString = s.toString().replace("$", "").replace(",", "").replace(".", "")
                    if (cleanString.isNotEmpty()) {
                        val parsed    = cleanString.toDouble() / 100
                        val formatted = NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(parsed)
                        current = formatted
                        montoInput.setText(formatted)
                        montoInput.setSelection(formatted.length)
                    }
                    montoInput.addTextChangedListener(this)
                }
            }
        })
    }

    private fun goHome() {
        val intent = Intent(this, Activity3PantallaDeInicio::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() { goHome() }
}
