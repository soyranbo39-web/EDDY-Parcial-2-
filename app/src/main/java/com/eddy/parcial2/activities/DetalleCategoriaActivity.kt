package com.eddy.parcial2.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eddy.parcial2.R
import com.eddy.parcial2.databinding.ActivityDetalleCategoriaBinding
import com.eddy.parcial2.MovimientoAdapter
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.models.Movimiento
import kotlinx.coroutines.launch

class DetalleCategoriaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleCategoriaBinding
    private lateinit var db: AppDatabase
    private var lista = mutableListOf<Movimiento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetalleCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Detalles"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = AppDatabase.getDatabase(this)

        val categoria = intent.getStringExtra("categoria") ?: "Categoría"
        val total     = intent.getDoubleExtra("total", 0.0)

        binding.txtCategoria.text = categoria
        binding.txtTotal.text     = "Total: $$total"

        lifecycleScope.launch {
            lista = db.movimientoDao().getMovimientosPorCategoria(categoria).toMutableList()
            configurarRecycler()
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.ordenCuenta   -> lista = lista.sortedBy { it.nombreCuenta }.toMutableList()
                R.id.ordenFecha    -> lista = lista.sortedByDescending { "%04d%02d%02d".format(it.ano, it.mes, it.dia) }.toMutableList()
                R.id.ordenCantidad -> lista = lista.sortedByDescending { it.cantidad }.toMutableList()
            }
            configurarRecycler()
            true
        }
    }

    private fun configurarRecycler() {
        binding.rvMovimientos.layoutManager = LinearLayoutManager(this)
        binding.rvMovimientos.adapter = MovimientoAdapter(lista) { movimiento ->
            lifecycleScope.launch {
                db.movimientoDao().eliminarPorId(movimiento.id)
            }
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
}
