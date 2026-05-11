package com.eddy.parcial2.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.eddy.parcial2.adapters.CategoriaAdapter
import com.eddy.parcial2.databinding.ActivityReporteCategoriasBinding
import com.eddy.parcial2.utils.DatosDummy

class ReporteCategoriasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReporteCategoriasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReporteCategoriasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Reporte por categorías"

        configurarSpinners()
        configurarRecycler()
    }

    private fun configurarSpinners() {

        val cuentas = listOf(
            "Todas",
            "Efectivo",
            "T. Débito",
            "T. Crédito",
            "Vales"
        )

        val tipos = listOf(
            "Gasto",
            "Ingreso"
        )

        val anios = listOf(
            "2024",
            "2025",
            "2026"
        )

        val meses = listOf(
            "Enero",
            "Febrero",
            "Marzo",
            "Abril",
            "Mayo",
            "Junio",
            "Julio",
            "Agosto",
            "Septiembre",
            "Octubre",
            "Noviembre",
            "Diciembre"
        )

        binding.spCuenta.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            cuentas
        )

        binding.spTipo.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            tipos
        )

        binding.spAnio.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            anios
        )

        binding.spMes.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            meses
        )
    }

    private fun configurarRecycler() {

        val lista = DatosDummy.obtenerCategorias()
            .sortedByDescending { it.porcentaje }
            .toMutableList()

        binding.rvCategorias.layoutManager =
            LinearLayoutManager(this)

        binding.rvCategorias.adapter =
            CategoriaAdapter(lista)
    }
}