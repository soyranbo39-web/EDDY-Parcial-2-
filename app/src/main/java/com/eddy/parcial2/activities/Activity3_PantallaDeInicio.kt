package com.eddy.parcial2.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.eddy.parcial2.R
import com.eddy.parcial2.adapters.CategoriaAdapter
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.databinding.Activity3PantalladeinicioBinding
import com.eddy.parcial2.models.CategoriaResumen

class Activity3PantallaDeInicio : AppCompatActivity() {

    private lateinit var binding: Activity3PantalladeinicioBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = Activity3PantalladeinicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(applicationContext)

        val ingresosDao = db.ingresosDao()
        val gastosDao = db.gastosDao()

        val opcionesTipoMovimiento = arrayOf("Todas", "Debito", "Crédito", "Vales")

        val adapterTipos = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            opcionesTipoMovimiento
        )

        adapterTipos.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.tipoMovimeinto.adapter = adapterTipos

        val years = (2026 downTo 2010).map { it.toString() }

        val adapterYear = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            years
        )

        adapterYear.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.aOSpinnenr.adapter = adapterYear

        val meses = arrayOf(
            "Enero", "Febrero", "Marzo", "Abril",
            "Mayo", "Junio", "Julio", "Agosto",
            "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )

        val adapterMes = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            meses
        )

        adapterMes.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.mesSpinner.adapter = adapterMes

        val ingresosTotal = try {
            ingresosDao.getIngresosMontos().sum()
        } catch (e: Exception) {
            0
        }

        val gastosPorCategoria = try {
            gastosDao.getGastosPorCategoria()
        } catch (e: Exception) {
            emptyList()
        }

        val gastosTotal = gastosPorCategoria.sumOf { it.total }

        binding.ingreso.text = "$ingresosTotal"
        binding.saldoAnterior.text = "$ingresosTotal"
        binding.gastos.text = "$gastosTotal"
        binding.saldoActual.text = "${ingresosTotal - gastosTotal}"

        val max = gastosPorCategoria.maxOfOrNull { it.total } ?: 1

        val categorias = gastosPorCategoria.map { item ->
            CategoriaResumen(
                icono = when (item.categoria) {
                    "Comida" -> R.drawable.ic_food
                    "Gasolina" -> R.drawable.ic_gas
                    "Despensa" -> R.drawable.ic_wallet
                    else -> R.drawable.ic_wallet
                },
                nombre = item.categoria,
                movimientos = 1,
                total = item.total.toDouble(),
                porcentaje = ((item.total.toDouble() / max) * 100).toInt()
            )
        }.toMutableList()

        binding.categoriaRecycleView.layoutManager = LinearLayoutManager(this)
        binding.categoriaRecycleView.adapter = CategoriaAdapter(categorias)
    }
}