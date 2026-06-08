package com.eddy.parcial2.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eddy.parcial2.Login.LoginActivity
import com.eddy.parcial2.Pantalla13
import com.eddy.parcial2.Pantalla7
import com.eddy.parcial2.R
import com.eddy.parcial2.adapters.CategoriaAdapter
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.data.UserRepository
import com.eddy.parcial2.databinding.ActivityReporteCategoriasBinding
import com.eddy.parcial2.models.CategoriaResumen
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class ReporteCategoriasActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityReporteCategoriasBinding
    private lateinit var db: AppDatabase

    private val todasLasCategorias = listOf(
        "Comida", "Servicios", "Transporte", "Suscripciones",
        "Casa", "Ropa", "Gasolina", "Despensa"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReporteCategoriasBinding.inflate(layoutInflater)
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

        binding.botonTopRegresar.setOnClickListener {
            irAlInicio()
        }

        configurarSpinners()
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
            R.id.nav_categorias -> { }
            R.id.nav_mantenimiento_categorias -> startActivity(Intent(this, com.eddy.parcial2.Pantalla11::class.java))
            R.id.nav_ayuda -> Toast.makeText(this, "ño quiello ayudate :(", Toast.LENGTH_SHORT).show()
            R.id.nav_acerca_de -> startActivity(Intent(this, Pantalla13::class.java))
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

    private fun configurarSpinners() {
        val cuentas = listOf("Todas", "Efectivo", "T. Débito", "T. Crédito", "Vales")
        val tipos = listOf("Gasto", "Ingreso")
        val anios = listOf("2024", "2025", "2026")
        val meses = listOf("Enero","Febrero","Marzo","Abril","Mayo","Junio", "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre")

        binding.spCuenta.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cuentas)
        binding.spTipo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)
        binding.spAnio.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, anios)
        binding.spMes.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, meses)
    }

    private fun cargarCategorias() {
        lifecycleScope.launch {
            val gastosPorCategoria = db.movimientoDao().getGastosPorCategoria()
            val gastoMap = gastosPorCategoria.associateBy { it.categoria }
            val max = gastosPorCategoria.maxOfOrNull { it.total } ?: 1.0

            val categoriasEnDB = db.movimientoDao().getCategorias()
            val todasCategorias = (todasLasCategorias + categoriasEnDB).distinct()

            val lista = todasCategorias.map { nombreCat ->
                val item = gastoMap[nombreCat]
                CategoriaResumen(
                    icono = when (nombreCat) {
                        "Comida" -> R.drawable.ic_food
                        "Gasolina" -> R.drawable.ic_gas
                        "Casa" -> R.drawable.ic_house
                        "Ropa" -> R.drawable.ic_clothes
                        else -> R.drawable.ic_wallet
                    },
                    nombre = nombreCat,
                    movimientos = item?.movimientos ?: 0,
                    total = item?.total ?: 0.0,
                    porcentaje = if (item != null) ((item.total / max) * 100).toInt() else 0
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

    @Deprecated("")
    override fun onBackPressed() { goHome() }
    override fun onResume() {
        super.onResume()
        cargarCategorias()
    }
}
