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
import com.eddy.parcial2.activities.ReporteCategoriasActivity
import com.eddy.parcial2.Login.LoginActivity
import com.eddy.parcial2.adapters.CategoriaMantenimientoAdapter
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.data.UserRepository
import com.eddy.parcial2.databinding.ActivityPantalla11Binding
import com.eddy.parcial2.models.Categoria
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import kotlinx.coroutines.launch

class Pantalla11 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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

        binding.navView.setNavigationItemSelectedListener(this)
        loadUserDataInDrawer()

        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        setupRecyclerView()

        binding.botonTopRegresar.setOnClickListener {
            irAlInicio()
        }

        binding.botonTopAgregar.setOnClickListener {
            Pantalla12.newInstance().show(supportFragmentManager, "Pantalla12")
        }

        cargarCategorias()
    }

    private fun loadUserDataInDrawer() {
        val prefs = getSharedPreferences("session_prefs", MODE_PRIVATE)
        val email = prefs.getString("user_email", null) ?: return
        if (binding.navView.headerCount == 0) return

        val headerView = binding.navView.getHeaderView(0)
        val ivAvatar   = headerView.findViewById<ImageView>(R.id.ivUserAvatar)
        val tvUsername = headerView.findViewById<TextView>(R.id.tvHeaderUsername)
        val tvEmail    = headerView.findViewById<TextView>(R.id.tvHeaderEmail)

        tvEmail.text = email
        val userRepository = UserRepository(db.userDao())
        lifecycleScope.launch {
            runCatching { userRepository.getUserByEmail(email) }.onSuccess { user ->
                user?.let {
                    tvUsername.text = it.username
                    tvEmail.text    = it.email
                    ivAvatar.setImageResource(when (it.avatarId) {
                        1 -> R.drawable.abrahan1
                        2 -> R.drawable.abraham2
                        3 -> R.drawable.abraham3
                        4 -> R.drawable.abraham4
                        else -> R.drawable.ic_launcher_foreground
                    })
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_inicio -> irAlInicio()
            R.id.nav_movimientos -> startActivity(Intent(this, Pantalla7::class.java))
            R.id.nav_cuentas -> startActivity(Intent(this, com.eddy.parcial2.Pantalla9.Pantalla9::class.java))
            R.id.nav_categorias -> startActivity(Intent(this, ReporteCategoriasActivity::class.java))
            R.id.nav_mantenimiento_categorias -> { }
            R.id.nav_ayuda -> Toast.makeText(this, "ño quiello ayudate :(", Toast.LENGTH_SHORT).show()
            R.id.nav_acerca_de -> startActivity(Intent(this, Pantalla13::class.java))
            R.id.nav_gestion_grupos -> startActivity(Intent(this, com.eddy.parcial2.FinanzasCompartidas.PantallaA::class.java))
            R.id.nav_logout -> logout()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        getSharedPreferences("session_prefs", MODE_PRIVATE).edit {
            putBoolean("is_logged", false).remove("user_email")
        }
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun irAlInicio() {
        val intent = Intent(this, Activity3PantallaDeInicio::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            irAlInicio()
        }
    }
}
