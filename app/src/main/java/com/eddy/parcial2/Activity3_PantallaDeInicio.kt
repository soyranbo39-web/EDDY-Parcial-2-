package com.eddy.parcial2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.eddy.parcial2.databinding.ActivityMainBinding
import android.widget.Spinner
import android.widget.TextView

class Activity3_PantallaDeInicio : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tipoMovimientoSp: Spinner
    private lateinit var ingresoBt : Button
    private lateinit var retiroBt : Button
    private lateinit var tranferenciaBt : Button
    private lateinit var yearSp: Spinner
    private lateinit var monthSp: Spinner

    private lateinit var ingresoTx: TextView
    private lateinit var saldoAnteriorTx: TextView
    private lateinit var gastosTx: TextView
    private lateinit var saldoActualTx: TextView
    private lateinit var categoriaRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tipoMovimientoSp = findViewById(R.id.tipoMovimeinto)
        ingresoBt = findViewById(R.id.ingresoButton)
        retiroBt = findViewById(R.id.retiroButton)
        tranferenciaBt = findViewById(R.id.transefenciaButton)
        yearSp = findViewById(R.id.añoSpinnenr)
        monthSp = findViewById(R.id.mesSpinner)
        ingresoTx = findViewById(R.id.ingreso)
        saldoAnteriorTx = findViewById(R.id.saldoAnterior)
        gastosTx = findViewById(R.id.gastos)
        saldoActualTx = findViewById(R.id.saldoActual)
        categoriaRV = findViewById(R.id.categoriaRecycleView)

    }
}