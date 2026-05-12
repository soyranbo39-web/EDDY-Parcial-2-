package com.eddy.parcial2.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.eddy.parcial2.R
import com.eddy.parcial2.adapters.MovimientoAdapter
import com.eddy.parcial2.databinding.ActivityDetalleCategoriaBinding
import com.eddy.parcial2.models.Movimiento
import com.eddy.parcial2.utils.DatosDummy

class DetalleCategoriaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleCategoriaBinding

    private var lista = mutableListOf<Movimiento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetalleCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Detalles"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val categoria =
            intent.getStringExtra("categoria")
                ?: "Categoría"

        val total =
            intent.getDoubleExtra("total", 0.0)

        binding.txtCategoria.text = categoria
        binding.txtTotal.text = "Total: $$total"

        lista = if (savedInstanceState != null) {

            savedInstanceState.getParcelableArrayList(
                "listaMovimientos",
                Movimiento::class.java
            )?.toMutableList()

                ?: mutableListOf()

        } else {

            DatosDummy.obtenerMovimientos()
        }

        configurarRecycler()

        binding.toolbar.setOnMenuItemClickListener {

            when (it.itemId) {

                R.id.ordenCuenta -> {

                    lista = lista.sortedBy {
                            mov -> mov.cuenta
                    }.toMutableList()
                }

                R.id.ordenFecha -> {

                    lista = lista.sortedByDescending {
                            mov -> mov.fecha
                    }.toMutableList()
                }

                R.id.ordenCantidad -> {

                    lista = lista.sortedByDescending {
                            mov -> mov.cantidad
                    }.toMutableList()
                }
            }

            configurarRecycler()

            true
        }
    }

    private fun configurarRecycler() {

        binding.rvMovimientos.layoutManager =
            LinearLayoutManager(this)

        binding.rvMovimientos.adapter =
            MovimientoAdapter(lista)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList(
            "listaMovimientos",
            ArrayList(lista)
        )
    }

    override fun onSupportNavigateUp(): Boolean {

        finish()

        return true
    }
}