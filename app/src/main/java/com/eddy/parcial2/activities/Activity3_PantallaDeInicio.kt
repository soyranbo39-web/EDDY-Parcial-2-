package com.eddy.parcial2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eddy.parcial2.Login.LoginActivity
import com.eddy.parcial2.Pantalla7
import com.eddy.parcial2.R
import com.eddy.parcial2.adapters.CategoriaAdapter
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.data.UserRepository
import com.eddy.parcial2.databinding.Activity3PantalladeinicioBinding
import com.eddy.parcial2.models.CategoriaResumen
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import androidx.core.content.edit

class Activity3PantallaDeInicio : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: Activity3PantalladeinicioBinding
    private lateinit var db: AppDatabase

    companion object {
        private const val PREFS = "session_prefs"
        private const val KEY_LOGGED = "is_logged"
        private const val KEY_EMAIL = "user_email"
        const val EXTRA_TIPO_MOVIMIENTO = "tipo_movimiento"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = Activity3PantalladeinicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(applicationContext)

        // --- Drawer setup ---
        binding.navView.setNavigationItemSelectedListener(this)
        loadUserDataInDrawer()

        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        // --- Spinners ---
        val opcionesTipoMovimiento = arrayOf("Todas", "Debito", "Crédito", "Vales")
        val adapterTipos = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            opcionesTipoMovimiento
        )
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tipoMovimeinto.adapter = adapterTipos

        val years = (2026 downTo 2010).map { it.toString() }
        val adapterYear = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            years
        )
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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
        adapterMes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mesSpinner.adapter = adapterMes

        // --- Buttons to Activity4 ---
        binding.ingresoButton.setOnClickListener {
            val intent = Intent(this, Activity4AgregarMovimiento::class.java)
            intent.putExtra(EXTRA_TIPO_MOVIMIENTO, "Ingreso")
            startActivity(intent)
        }

        binding.retiroButton.setOnClickListener {
            val intent = Intent(this, Activity4AgregarMovimiento::class.java)
            intent.putExtra(EXTRA_TIPO_MOVIMIENTO, "Gasto")
            startActivity(intent)
        }

        binding.transferenciaButton.setOnClickListener {
            val intent = Intent(this, Activity4AgregarMovimiento::class.java)
            intent.putExtra(EXTRA_TIPO_MOVIMIENTO, "Transferencia")
            startActivity(intent)
        }

        cargarResumen()
    }

    private fun loadUserDataInDrawer() {
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        val email = prefs.getString(KEY_EMAIL, null) ?: return

        if (binding.navView.headerCount == 0) return

        val headerView = binding.navView.getHeaderView(0)
        val ivAvatar = headerView.findViewById<ImageView>(R.id.ivUserAvatar)
        val tvUsername = headerView.findViewById<TextView>(R.id.tvHeaderUsername)
        val tvEmail = headerView.findViewById<TextView>(R.id.tvHeaderEmail)

        tvEmail.text = email

        val userRepository = UserRepository(db.userDao())
        lifecycleScope.launch {
            runCatching {
                userRepository.getUserByEmail(email)
            }.onSuccess { user ->
                user?.let {
                    tvUsername.text = it.username
                    tvEmail.text = it.email
                    val avatarRes = when (it.avatarId) {
                        1 -> R.drawable.abrahan1
                        2 -> R.drawable.abraham2
                        3 -> R.drawable.abraham3
                        4 -> R.drawable.abraham4
                        else -> R.drawable.ic_launcher_foreground
                    }
                    ivAvatar.setImageResource(avatarRes)
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_inicio -> {
                // Already here
            }
            R.id.nav_movimientos -> {
                startActivity(Intent(this, Pantalla7::class.java))
            }
            R.id.nav_cuentas -> {
                Toast.makeText(this, "Cuentas", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_categorias -> {
                startActivity(Intent(this, ReporteCategoriasActivity::class.java))
            }
            R.id.nav_ayuda -> {
                Toast.makeText(this, "ño quiello ayudate :(", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_acerca_de -> {
                Toast.makeText(this, "Acerca de", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> logout()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        prefs.edit {
            putBoolean(KEY_LOGGED, false).remove(KEY_EMAIL)
        }
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun cargarResumen() {
        lifecycleScope.launch {
            val movimientoDao = db.movimientoDao()

            val ingresosTotal = movimientoDao.getTotalIngresos() ?: 0.0
            val gastosPorCategoria = movimientoDao.getGastosPorCategoria()
            val gastosTotal = gastosPorCategoria.sumOf { it.total }

            binding.ingreso.text = ingresosTotal.toInt().toString()
            binding.saldoAnterior.text = ingresosTotal.toInt().toString()
            binding.gastos.text = gastosTotal.toInt().toString()
            binding.saldoActual.text = (ingresosTotal - gastosTotal).toInt().toString()

            val max = gastosPorCategoria.maxOfOrNull { it.total } ?: 1.0

            val categorias = gastosPorCategoria.map { item ->
                CategoriaResumen(
                    icono = when (item.categoria) {
                        "Comida" -> R.drawable.ic_food
                        "Gasolina" -> R.drawable.ic_gas
                        "Casa" -> R.drawable.ic_house
                        "Ropa" -> R.drawable.ic_clothes
                        "Despensa" -> R.drawable.ic_wallet
                        else -> R.drawable.ic_wallet
                    },
                    nombre = item.categoria,
                    movimientos = item.movimientos,
                    total = item.total,
                    porcentaje = ((item.total / max) * 100).toInt()
                )
            }.toMutableList()

            binding.categoriaRecycleView.layoutManager = LinearLayoutManager(this@Activity3PantallaDeInicio)
            binding.categoriaRecycleView.adapter = CategoriaAdapter(categorias)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarResumen()
    }
}
