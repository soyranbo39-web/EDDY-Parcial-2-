package com.eddy.parcial2.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eddy.parcial2.R
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.models.Movimiento
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

    companion object {
        const val EXTRA_TIPO_MOVIMIENTO = "tipo_movimiento"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity4layout)

        db = AppDatabase.getDatabase(applicationContext)

        tipoMovimientoSp = findViewById(R.id.tipoMovimeinto)
        cantidadEt       = findViewById(R.id.cantidad)
        cuentaSp         = findViewById(R.id.cuenta)
        categoriaSp      = findViewById(R.id.categoria)
        cuentaOrigenSp   = findViewById(R.id.cuentaOrigen)
        cuentaDestinoSp  = findViewById(R.id.cuentaDestino)
        descripcionEt    = findViewById(R.id.descripcion)
        fechaBtn         = findViewById(R.id.fecha)
        guardarBt        = findViewById(R.id.guardar)

        val tiposTransferencia = listOf("Ingreso", "Gasto", "Transferencia")
        val tiposCuenta        = listOf("Efectivo", "TarjetaDebito", "TarjetaCredito")
        val categorias         = listOf("Comida", "Servicios", "Transporte", "Suscripciones",
                                        "Casa", "Ropa", "Gasolina", "Despensa")

        val tipoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tiposTransferencia)
        tipoMovimientoSp.adapter = tipoAdapter

        val tipoFromIntent = intent.getStringExtra(EXTRA_TIPO_MOVIMIENTO)
        if (tipoFromIntent != null) {
            val pos = tiposTransferencia.indexOf(tipoFromIntent)
            if (pos >= 0) tipoMovimientoSp.setSelection(pos)
        }

        cuentaSp.adapter        = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tiposCuenta)
        categoriaSp.adapter     = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)
        cuentaOrigenSp.adapter  = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tiposCuenta)
        cuentaDestinoSp.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tiposCuenta)

        val cal = Calendar.getInstance()
        ano = cal.get(Calendar.YEAR)
        mes = cal.get(Calendar.MONTH) + 1
        dia = cal.get(Calendar.DAY_OF_MONTH)
        fechaBtn.text = "$dia/$mes/$ano"

        fechaBtn.setOnClickListener {
            DatePickerDialog(this, { _, year, month, day ->
                ano = year; mes = month + 1; dia = day
                fechaBtn.text = "$dia/$mes/$ano"
            }, ano, mes - 1, dia).show()
        }

        guardarBt.setOnClickListener {
            val cantidadStr = cantidadEt.text.toString().trim()
            if (cantidadStr.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa una cantidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val cantidad = cantidadStr.toDoubleOrNull()
            if (cantidad == null || cantidad <= 0) {
                Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tipoCuenta = cuentaSp.selectedItem.toString()
            val movimiento = Movimiento(
                ano              = ano,
                mes              = mes,
                dia              = dia,
                cantidad         = cantidad,
                descripcion      = descripcionEt.text.toString().trim(),
                nombreCuenta     = tipoCuenta,
                tipoCuenta       = tipoCuenta,
                tipoTransferencia = tipoMovimientoSp.selectedItem.toString(),
                categoria        = categoriaSp.selectedItem.toString()
            )

            lifecycleScope.launch {
                db.movimientoDao().insertar(movimiento)
                Toast.makeText(this@Activity4AgregarMovimiento, "Movimiento guardado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
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
