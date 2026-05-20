package com.eddy.parcial2.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
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
import java.util.Calendar

class Activity3PantallaDeInicio : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: Activity3PantalladeinicioBinding
    private lateinit var db: AppDatabase
    private var filtroAno: Int = -1
    private var filtroMes: Int = -1
    private val anosRaw = mutableListOf<Int>()   // -1 = todos
    private val mesesRaw = mutableListOf<Int>()  // -1 = todos
    private var spinnersListos = false

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

        binding.navView.setNavigationItemSelectedListener(this)
        loadUserDataInDrawer()
        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        val opcionesTipoMovimiento = arrayOf("Todas", "Debito", "Crédito", "Vales")
        binding.tipoMovimeinto.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            opcionesTipoMovimiento
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        anosRaw.clear()
        anosRaw.add(-1) // todos
        for (y in currentYear downTo 2010) anosRaw.add(y)

        val anosTexto = anosRaw.map { if (it == -1) "Todos" else it.toString() }
        binding.aOSpinnenr.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            anosTexto
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        mesesRaw.clear()
        mesesRaw.add(-1) // todos
        for (m in 1..12) mesesRaw.add(m)

        val mesesTexto = mesesRaw.map { m ->
            when (m) {
                -1 -> "Todos"
                1 -> "Enero"; 2 -> "Febrero"; 3 -> "Marzo"; 4 -> "Abril"
                5 -> "Mayo"; 6 -> "Junio"; 7 -> "Julio"; 8 -> "Agosto"
                9 -> "Septiembre"; 10 -> "Octubre"; 11 -> "Noviembre"; 12 -> "Diciembre"
                else -> m.toString()
            }
        }
        binding.mesSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            mesesTexto
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        val anoPos = anosRaw.indexOf(currentYear).takeIf { it >= 0 } ?: 0
        val mesPos = mesesRaw.indexOf(currentMonth).takeIf { it >= 0 } ?: 0
        filtroAno = currentYear
        filtroMes = currentMonth

        spinnersListos = false
        binding.aOSpinnenr.setSelection(anoPos, false)
        binding.mesSpinner.setSelection(mesPos, false)
        spinnersListos = true

        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                if (!spinnersListos) return
                filtroAno = anosRaw[binding.aOSpinnenr.selectedItemPosition]
                filtroMes = mesesRaw[binding.mesSpinner.selectedItemPosition]
                cargarResumen()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.aOSpinnenr.onItemSelectedListener = spinnerListener
        binding.mesSpinner.onItemSelectedListener = spinnerListener

        binding.ingresoButton.setOnClickListener {
            startActivity(Intent(this, Activity4AgregarMovimiento::class.java)
                .putExtra(EXTRA_TIPO_MOVIMIENTO, "Ingreso"))
        }
        binding.retiroButton.setOnClickListener {
            startActivity(Intent(this, Activity4AgregarMovimiento::class.java)
                .putExtra(EXTRA_TIPO_MOVIMIENTO, "Gasto"))
        }
        binding.transferenciaButton.setOnClickListener {
            startActivity(Intent(this, Activity4AgregarMovimiento::class.java)
                .putExtra(EXTRA_TIPO_MOVIMIENTO, "Transferencia"))
        }

        cargarResumen()
    }

    private fun loadUserDataInDrawer() {
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        val email = prefs.getString(KEY_EMAIL, null) ?: return
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
            R.id.nav_inicio -> { }
            R.id.nav_movimientos -> startActivity(Intent(this, Pantalla7::class.java))
            R.id.nav_cuentas -> Toast.makeText(this, "Cuentas", Toast.LENGTH_SHORT).show()
            R.id.nav_categorias -> startActivity(Intent(this, ReporteCategoriasActivity::class.java))
            R.id.nav_ayuda -> Toast.makeText(this, "ño quiello ayudate :(", Toast.LENGTH_SHORT).show()
            R.id.nav_acerca_de -> Toast.makeText(this, "Acerca de", Toast.LENGTH_SHORT).show()
            R.id.nav_logout -> logout()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun logout() {
        getSharedPreferences(PREFS, MODE_PRIVATE).edit {
            putBoolean(KEY_LOGGED, false).remove(KEY_EMAIL)
        }
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    @Deprecated("")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun cargarResumen() {
        lifecycleScope.launch {
            val dao = db.movimientoDao()

            val ingresosTotal = dao.getTotalIngresosFiltrado(filtroAno, filtroMes) ?: 0.0
            val gastosPorCat = dao.getGastosPorCategoriaFiltrado(filtroAno, filtroMes)
            val gastosTotal = gastosPorCat.sumOf { it.total }

            binding.ingreso.text = "$${"%.2f".format(ingresosTotal)}"
            binding.saldoAnterior.text = "$${"%.2f".format(ingresosTotal)}"
            binding.gastos.text = "$${"%.2f".format(gastosTotal)}"
            binding.saldoActual.text  = "$${"%.2f".format(ingresosTotal - gastosTotal)}"

            val max = gastosPorCat.maxOfOrNull { it.total } ?: 1.0

            val categorias = gastosPorCat.map { item ->
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

            binding.categoriaRecycleView.layoutManager =
                LinearLayoutManager(this@Activity3PantallaDeInicio)
            binding.categoriaRecycleView.adapter = CategoriaAdapter(categorias)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarResumen()
    }
}
