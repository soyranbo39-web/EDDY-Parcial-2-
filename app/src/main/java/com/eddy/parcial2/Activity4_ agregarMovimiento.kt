package com.eddy.parcial2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.eddy.parcial2.databinding.Activity4layoutBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.app.DatePickerDialog
import java.util.Calendar

class Activity4_agregarMovimiento : AppCompatActivity() {
    private lateinit var tipoMovimientoSp: Spinner
    private lateinit var cantidadEt: EditText

    private lateinit var cuentaSectionLy: LinearLayout

    private lateinit var cuentaSp: Spinner
    private lateinit var categoriaSp: Spinner
    private lateinit var cuentaOrigenSp: Spinner
    private lateinit var cuentaDestinoSp: Spinner

    private lateinit var descripcionEt: EditText
    private lateinit var fechaDp: Button

    private lateinit var guardarBt: Button

    private lateinit var binding: Activity4layoutBinding
    private var fechaCompleta: Date? = null

    var selectedAno = 0
    var selectedDia = 0
    var selectedMes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = Activity4layoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tipoMovimientoSp = findViewById(R.id.tipoMovimeinto)
        cantidadEt = findViewById(R.id.cantidad)

        cuentaSectionLy = findViewById(R.id.cuentaSection)

        cuentaSp = findViewById(R.id.cuenta)
        categoriaSp = findViewById(R.id.categoria)
        cuentaOrigenSp = findViewById(R.id.cuentaOrigen)
        cuentaDestinoSp = findViewById(R.id.cuentaDestino)

        descripcionEt = findViewById(R.id.descripcion)
        fechaDp = findViewById(R.id.fecha)

        guardarBt = findViewById(R.id.guardar)

        val movimientos = arrayOf(
            "Ingreso",
            "Retiro",
            "Transferencia"
        )

        val cuentas = arrayOf(
            "Cuenta BBVA",
            "Cuenta Banamex",
            "Cuenta Santander"
        )

        val categorias = arrayOf(
            "Comida",
            "Transporte",
            "Servicios"
        )

        val movimientoAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            movimientos
        )

        movimientoAdapter.setDropDownViewResource(

            android.R.layout.simple_spinner_dropdown_item
        )

        tipoMovimientoSp.adapter = movimientoAdapter

        val cuentaAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            cuentas
        )

        cuentaAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        cuentaSp.adapter = cuentaAdapter
        cuentaOrigenSp.adapter = cuentaAdapter
        cuentaDestinoSp.adapter = cuentaAdapter

        val categoriaAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categorias
        )

        categoriaAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        categoriaSp.adapter = categoriaAdapter

        fechaDp.setOnClickListener {

            val calendar = Calendar.getInstance()

            val year = if (selectedAno != 0)
                selectedAno
            else
                calendar.get(Calendar.YEAR)

            val month = if (selectedMes != 0)
                selectedMes - 1
            else
                calendar.get(Calendar.MONTH)

            val day = if (selectedDia != 0)
                selectedDia
            else
                calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, y, m, d ->

                selectedAno = y
                selectedMes = m + 1
                selectedDia = d

                val fechaString = "$selectedDia/$selectedMes/$selectedAno"

                fechaDp.text = fechaString

                val formato = SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                )

                fechaCompleta = formato.parse(fechaString)!!

            }, year, month, day).show()
        }

        guardarBt.setOnClickListener {

            val tipoMovimiento = tipoMovimientoSp.selectedItem.toString()
            val cantidad = cantidadEt.text.toString().toIntOrNull() ?: 0

            val cuenta = cuentaSp.selectedItem.toString()
            val categoria = categoriaSp.selectedItem.toString()

            val cuentaOrigen = cuentaOrigenSp.selectedItem.toString()
            val cuentaDestino = cuentaDestinoSp.selectedItem.toString()

            val descripcion = descripcionEt.text.toString()


            Movimiento(
                id = 1,
                tipoMovimiento = tipoMovimiento,
                cantidad = cantidad,
                cuenta = cuenta,
                categoria = categoria,
                cuentaOrigen = cuentaOrigen,
                cuentaDestino = cuentaDestino,
                descripcion = descripcion,
                fecha = fechaCompleta)

            
        }
    }
}