package com.eddy.parcial2.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eddy.parcial2.R
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.MovimientoContenedor
import kotlinx.coroutines.launch
import java.util.Calendar

class Activity4AgregarMovimiento : AppCompatActivity() {

    private lateinit var tipoMovimientoSp: Spinner
    private lateinit var cantidadEt: EditText

    private lateinit var cuentaSp: Spinner
    private lateinit var categoriaSp: Spinner
    private lateinit var cuentaOrigenSp: Spinner
    private lateinit var cuentaDestinoSp: Spinner

    private lateinit var descripcionEt: EditText
    private lateinit var fechaBtn: Button
    private lateinit var guardarBt: Button

    private lateinit var db: AppDatabase

    private var ano = 0
    private var mes = 0
    private var dia = 0
    private var fechaTexto = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity4layout)

        db = AppDatabase.getDatabase(applicationContext)

        tipoMovimientoSp = findViewById(R.id.tipoMovimeinto)
        cantidadEt = findViewById(R.id.cantidad)

        cuentaSp = findViewById(R.id.cuenta)
        categoriaSp = findViewById(R.id.categoria)
        cuentaOrigenSp = findViewById(R.id.cuentaOrigen)
        cuentaDestinoSp = findViewById(R.id.cuentaDestino)

        descripcionEt = findViewById(R.id.descripcion)
        fechaBtn = findViewById(R.id.fecha)
        guardarBt = findViewById(R.id.guardar)

        fechaBtn.setOnClickListener {
            val cal = Calendar.getInstance()

            val y = if (ano != 0) ano else cal.get(Calendar.YEAR)
            val m = if (mes != 0) mes - 1 else cal.get(Calendar.MONTH)
            val d = if (dia != 0) dia else cal.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, year, month, day ->
                ano = year
                mes = month + 1
                dia = day

                fechaTexto = "$dia/$mes/$ano"
                fechaBtn.text = fechaTexto
            }, y, m, d).show()
        }

        guardarBt.setOnClickListener {

            val movimiento = MovimientoContenedor(
                ano = ano,
                mes = mes,
                dia = dia,
                cantidad = cantidadEt.text.toString().toDoubleOrNull() ?: 0.0,
                descripcion = descripcionEt.text.toString(),
                nombreCuenta = cuentaSp.selectedItem.toString(),
                tipoCuenta = cuentaOrigenSp.selectedItem.toString(),
                tipoTransferencia = tipoMovimientoSp.selectedItem.toString(),
                categoria = categoriaSp.selectedItem.toString()
            )

            lifecycleScope.launch {
                db.movimientoDao().insertar(movimiento)
            }
        }
    }
}