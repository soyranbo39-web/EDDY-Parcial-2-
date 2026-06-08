package com.eddy.parcial2.Pantalla9

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eddy.parcial2.activities.Activity3PantallaDeInicio
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.databinding.ActivityPantalla9Binding
import com.eddy.parcial2.Pantalla9.interfaces.ICuentaRepository
import com.eddy.parcial2.Pantalla9.models.Cuenta
import com.eddy.parcial2.Pantalla9.repository.CuentaRepository
import com.eddy.parcial2.R
import com.eddy.parcial2.activities.ReporteCategoriasActivity
import com.eddy.parcial2.Login.LoginActivity
import com.eddy.parcial2.Pantalla7
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.eddy.parcial2.data.UserRepository
import androidx.core.content.edit
import kotlinx.coroutines.launch

class Pantalla9 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityPantalla9Binding
    private lateinit var repositorio: ICuentaRepository
    private lateinit var adaptador: CuentaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPantalla9Binding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Cuentas"

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val barrasSistema = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(barrasSistema.left, barrasSistema.top, barrasSistema.right, barrasSistema.bottom)
            insets
        }

        val baseDatos = AppDatabase.getDatabase(this)
        repositorio = CuentaRepository(baseDatos.cuentaDao())

        binding.navView.setNavigationItemSelectedListener(this)
        loadUserDataInDrawer()

        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        configurarRecycler()

        binding.botonTopRegresar.setOnClickListener { irAlInicio() }

        binding.botonTopAgregar.setOnClickListener {
            startActivity(Intent(this, com.eddy.parcial2.Pantalla10::class.java))
            Toast.makeText(this, "Pantalla 10: Agregar cuenta", Toast.LENGTH_SHORT).show()
        }

        cargarCuentas()
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
        val userRepository = UserRepository(AppDatabase.getDatabase(this).userDao())
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
            R.id.nav_cuentas -> { }
            R.id.nav_categorias -> startActivity(Intent(this, ReporteCategoriasActivity::class.java))
            R.id.nav_mantenimiento_categorias -> startActivity(Intent(this, com.eddy.parcial2.Pantalla11::class.java))
            R.id.nav_ayuda -> Toast.makeText(this, "ño quiello ayudate :(", Toast.LENGTH_SHORT).show()
            R.id.nav_acerca_de -> Toast.makeText(this, "Acerca de", Toast.LENGTH_SHORT).show()
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

    private fun configurarRecycler() {
        adaptador = CuentaAdapter(mutableListOf()) { cuenta -> mostrarMenu(cuenta) }
        binding.recyclerCuentas.layoutManager = LinearLayoutManager(this)
        binding.recyclerCuentas.adapter = adaptador
    }

    private fun mostrarMenu(cuenta: Cuenta) {
        val vista = binding.recyclerCuentas
            .findViewHolderForItemId(cuenta.id.toLong())?.itemView ?: binding.recyclerCuentas

        val menuEmergente = PopupMenu(this, vista)
        menuEmergente.menu.add("Modificar")

        lifecycleScope.launch {
            val sinMovimientos = !repositorio.tieneMovimientos(cuenta.nombre)
            if (sinMovimientos) menuEmergente.menu.add("Eliminar")

            menuEmergente.setOnMenuItemClickListener { opcion ->
                when (opcion.title) {
                    "Modificar" -> {
                        // Abre Pantalla #10 se tiene que agregar logica de la pantalla 10 para modificar cuenta (por implementar) canto me estas desgarrando vicente no leas esto canto se vino aqui
                        Toast.makeText(this@Pantalla9, "Pantalla 10: Modificar cuenta", Toast.LENGTH_SHORT).show()
                    }
                    "Eliminar" -> eliminarCuenta(cuenta)
                }
                true
            }
            menuEmergente.show()
        }
    }

    private fun eliminarCuenta(cuenta: Cuenta) {
        lifecycleScope.launch {
            repositorio.eliminar(cuenta)
            cargarCuentas()
            Snackbar.make(binding.main, "Cuenta eliminada", Snackbar.LENGTH_LONG)
                .setAction("Deshacer") {
                    lifecycleScope.launch {
                        repositorio.insertar(cuenta)
                        cargarCuentas()
                    }
                }.show()
        }
    }

    fun cargarCuentas() {
        lifecycleScope.launch {
            adaptador.actualizarDatos(repositorio.obtenerCuentas())
        }
    }

    private fun irAlInicio() {
        val intent = Intent(this, Activity3PantallaDeInicio::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        cargarCuentas()
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
