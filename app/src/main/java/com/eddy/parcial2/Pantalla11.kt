package com.eddy.parcial2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eddy.parcial2.activities.Activity3PantallaDeInicio
import com.eddy.parcial2.adapters.CategoriaMantenimientoAdapter
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.databinding.ActivityPantalla11Binding
import com.eddy.parcial2.models.Categoria
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class Pantalla11 : AppCompatActivity() {

    private lateinit var binding: ActivityPantalla11Binding
    private lateinit var db: AppDatabase
    private lateinit var adapter: CategoriaMantenimientoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPantalla11Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = AppDatabase.getDatabase(this)

        setupRecyclerView()

        binding.botonTopRegresar.setOnClickListener {
            val intent = Intent(this, Activity3PantallaDeInicio::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        binding.botonTopAgregar.setOnClickListener {
            Pantalla12.newInstance().show(supportFragmentManager, "Pantalla12")
        }

        cargarCategorias()
    }

    private fun setupRecyclerView() {
        adapter = CategoriaMantenimientoAdapter(mutableListOf()) { categoria ->
            showMenu(categoria)
        }
        binding.recyclerCategorias.layoutManager = LinearLayoutManager(this)
        binding.recyclerCategorias.adapter = adapter
    }

    private fun showMenu(categoria: Categoria) {
        val view = binding.recyclerCategorias.findViewHolderForItemId(categoria.id.toLong())?.itemView ?: binding.recyclerCategorias
        val popup = PopupMenu(this, view)
        popup.menu.add("Modificar")
        
        lifecycleScope.launch {
            val count = db.categoriaDao().countMovimientosByCategoria(categoria.nombre)
            if (count == 0) {
                popup.menu.add("Eliminar")
            }

            popup.setOnMenuItemClickListener { item ->
                when (item.title) {
                    "Modificar" -> {
                        Pantalla12.newInstance(categoria.id).show(supportFragmentManager, "Pantalla12")
                    }
                    "Eliminar" -> {
                        eliminarCategoria(categoria)
                    }
                }
                true
            }
            popup.show()
        }
    }

    private fun eliminarCategoria(categoria: Categoria) {
        lifecycleScope.launch {
            db.categoriaDao().eliminar(categoria)
            cargarCategorias()
            
            Snackbar.make(binding.main, "Categoría eliminada", Snackbar.LENGTH_LONG)
                .setAction("Deshacer") {
                    lifecycleScope.launch {
                        db.categoriaDao().insertar(categoria)
                        cargarCategorias()
                    }
                }.show()
        }
    }

    fun cargarCategorias() {
        lifecycleScope.launch {
            val lista = db.categoriaDao().getCategoriasDesc()
            adapter.updateData(lista)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarCategorias()
    }
}
