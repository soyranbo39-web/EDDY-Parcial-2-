package com.eddy.parcial2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eddy.parcial2.R
import com.eddy.parcial2.adapters.CategoriaAdapter
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.databinding.ActivityReporteCategoriasBinding
import com.eddy.parcial2.models.CategoriaResumen
import kotlinx.coroutines.launch

class ReporteCategoriasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReporteCategoriasBinding
    private lateinit var db: AppDatabase

    private val todasLasCategorias = listOf(
        "Comida", "Servicios", "Transporte", "Suscripciones",
        "Casa", "Ropa", "Gasolina", "Despensa"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReporteCategoriasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)
        binding.toolbar.title = "Reporte por categorías"

        configurarSpinners()
        cargarCategorias()
    }

    private fun configurarSpinners() {
        val cuentas = listOf("Todas", "Efectivo", "T. Débito", "T. Crédito", "Vales")
        val tipos   = listOf("Gasto", "Ingreso")
        val anios   = listOf("2024", "2025", "2026")
        val meses   = listOf("Enero","Febrero","Marzo","Abril","Mayo","Junio",
                             "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre")

        binding.spCuenta.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cuentas)
        binding.spTipo.adapter   = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)
        binding.spAnio.adapter   = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, anios)
        binding.spMes.adapter    = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, meses)
    }

    private fun cargarCategorias() {
        lifecycleScope.launch {
            val gastosPorCategoria = db.movimientoDao().getGastosPorCategoria()
            val gastoMap = gastosPorCategoria.associateBy { it.categoria }
            val max      = gastosPorCategoria.maxOfOrNull { it.total } ?: 1.0

            val categoriasEnDB  = db.movimientoDao().getCategorias()
            val todasCategorias = (todasLasCategorias + categoriasEnDB).distinct()

            val lista = todasCategorias.map { nombreCat ->
                val item = gastoMap[nombreCat]
                CategoriaResumen(
                    icono = when (nombreCat) {
                        "Comida"   -> R.drawable.ic_food
                        "Gasolina" -> R.drawable.ic_gas
                        "Casa"     -> R.drawable.ic_house
                        "Ropa"     -> R.drawable.ic_clothes
                        else       -> R.drawable.ic_wallet
                    },
                    nombre      = nombreCat,
                    movimientos = item?.movimientos ?: 0,
                    total       = item?.total ?: 0.0,
                    porcentaje  = if (item != null) ((item.total / max) * 100).toInt() else 0
                )
            }.sortedByDescending { it.porcentaje }.toMutableList()

            binding.rvCategorias.layoutManager = LinearLayoutManager(this@ReporteCategoriasActivity)
            binding.rvCategorias.adapter = CategoriaAdapter(lista)
        }
    }

    private fun goHome() {
        val intent = Intent(this, Activity3PantallaDeInicio::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        goHome()
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() { goHome() }

    override fun onResume() {
        super.onResume()
        cargarCategorias()
    }
}
