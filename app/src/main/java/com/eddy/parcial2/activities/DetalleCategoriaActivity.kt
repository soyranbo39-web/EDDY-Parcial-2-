package com.eddy.parcial2.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.eddy.parcial2.R
import com.eddy.parcial2.databinding.ActivityDetalleCategoriaBinding
import com.eddy.parcial2.MovimientoContenedor
import com.eddy.parcial2.MovimientoAdapter

class DetalleCategoriaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleCategoriaBinding
    private var lista = mutableListOf<MovimientoContenedor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetalleCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Detalles"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val categoria = intent.getStringExtra("categoria") ?: "Categoría"
        val total = intent.getDoubleExtra("total", 0.0)

        binding.txtCategoria.text = categoria
        binding.txtTotal.text = "Total: $$total"

        configurarRecycler()

        binding.toolbar.setOnMenuItemClickListener { it ->

            when (it.itemId) {

                R.id.ordenCuenta -> {
                    lista = lista.sortedBy { it.nombreCuenta }.toMutableList()
                }

                R.id.ordenFecha -> {
                    lista = lista.sortedByDescending {
                        "${it.ano}-${it.mes}-${it.dia}"
                    }.toMutableList()
                }

                R.id.ordenCantidad -> {
                    lista = lista.sortedByDescending { it.cantidad }.toMutableList()
                }
            }

            configurarRecycler()
            true
        }
    }

    private fun configurarRecycler() {
        binding.rvMovimientos.layoutManager = LinearLayoutManager(this)
        binding.rvMovimientos.adapter = MovimientoAdapter(lista)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}