package com.eddy.parcial2.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.eddy.parcial2.R
import com.eddy.parcial2.adapters.MovimientoAdapter
import com.eddy.parcial2.databinding.ActivityDetalleCategoriaBinding
import com.eddy.parcial2.utils.DatosDummy

class DetalleCategoriaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleCategoriaBinding
    private var lista = DatosDummy.obtenerMovimientos()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetalleCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Detalle"

        val categoria =
            intent.getStringExtra("categoria")
                ?: "Categoría"

        val total =
            intent.getDoubleExtra("total", 0.0)

        binding.txtCategoria.text = categoria
        binding.txtTotal.text = "Total: $$total"

        configurarRecycler()
    }

    private fun configurarRecycler() {

        binding.rvMovimientos.layoutManager =
            LinearLayoutManager(this)

        binding.rvMovimientos.adapter =
            MovimientoAdapter(lista)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(
            R.menu.menu_detalle_categoria,
            menu
        )

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.ordenCuenta -> {

                lista = lista.sortedBy {
                    it.cuenta
                }.toMutableList()
            }

            R.id.ordenFecha -> {

                lista = lista.sortedByDescending {
                    it.fecha
                }.toMutableList()
            }

            R.id.ordenCantidad -> {

                lista = lista.sortedByDescending {
                    it.cantidad
                }.toMutableList()
            }
        }

        configurarRecycler()

        return true
    }
}