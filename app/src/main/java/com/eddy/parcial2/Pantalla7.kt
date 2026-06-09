package com.eddy.parcial2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eddy.parcial2.activities.Activity3PantallaDeInicio
import com.eddy.parcial2.activities.ReporteCategoriasActivity
import com.eddy.parcial2.Login.LoginActivity
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.data.UserRepository
import com.eddy.parcial2.databinding.ActivityPantalla7Binding
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import kotlinx.coroutines.launch

class Pantalla7 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var modoOrden = "fecha"

    private var lastCuenta = "Cuenta"
    private var lastAno = -1
    private var lastMes = -1

    private lateinit var db: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerCuenta: Spinner
    private lateinit var spinnerAno: Spinner
    private lateinit var spinnerMes: Spinner

    private var spinnersReady = false
    private lateinit var adapter: MovimientoAdapter
    private lateinit var binding: ActivityPantalla7Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityPantalla7Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = binding.recyclerListaMovimientos
        recyclerView.layoutManager = LinearLayoutManager(this)

        db = AppDatabase.getDatabase(this)

        binding.navView.setNavigationItemSelectedListener(this)
        loadUserDataInDrawer()

        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        adapter = MovimientoAdapter(mutableListOf()) { movimiento ->
            lifecycleScope.launch {
                db.movimientoDao().eliminarPorId(movimiento.id)
                cargarLista()
            }
        }
        recyclerView.adapter = adapter

        spinnerCuenta = binding.spinnerSortCuenta
        spinnerAno    = binding.spinnerSortAno
        spinnerMes    = binding.spinnerSortMes

        val sortButton = binding.botonTopSort
        val backButton = binding.botonTopRegresar

        sortButton.setOnClickListener {
            val popup = PopupMenu(this, sortButton)
            popup.menu.add(Menu.NONE, 1, 1, "Fecha")
            popup.menu.add(Menu.NONE, 2, 2, "Cuenta")
            popup.menu.add(Menu.NONE, 3, 3, "Cantidad")
            popup.setOnMenuItemClickListener {
                modoOrden = when (it.itemId) { 2 -> "cuenta"; 3 -> "cantidad"; else -> "fecha" }
                cargarLista()
                true
            }
            popup.show()
        }

        backButton.setOnClickListener { goHome() }

        cargarFiltros()
    }

    private fun goHome() {
        val intent = Intent(this, Activity3PantallaDeInicio::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
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
            R.id.nav_inicio -> goHome()
            R.id.nav_movimientos -> { }
            R.id.nav_cuentas -> startActivity(Intent(this, com.eddy.parcial2.Pantalla9.Pantalla9::class.java))
            R.id.nav_categorias -> startActivity(Intent(this, ReporteCategoriasActivity::class.java))
            R.id.nav_mantenimiento_categorias -> startActivity(Intent(this, com.eddy.parcial2.Pantalla11::class.java))
            R.id.nav_ayuda -> android.widget.Toast.makeText(this, "ño quiello ayudate :(", android.widget.Toast.LENGTH_SHORT).show()
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

    private fun cargarFiltros() {
        lifecycleScope.launch {
            val cuentas  = listOf("Cuenta") + db.movimientoDao().obtenerCuentas()
            val anosRaw  = listOf(-1) + db.movimientoDao().obtenerAnos()
            val mesesRaw = listOf(-1) + db.movimientoDao().obtenerMeses()

            val anosTexto  = anosRaw.map  { if (it == -1) "Año"  else it.toString() }
            val mesesTexto = mesesRaw.map { if (it == -1) "Mes"  else it.toString() }

            spinnerCuenta.adapter = ArrayAdapter(this@Pantalla7, android.R.layout.simple_spinner_dropdown_item, cuentas)
            spinnerAno.adapter    = ArrayAdapter(this@Pantalla7, android.R.layout.simple_spinner_dropdown_item, anosTexto)
            spinnerMes.adapter    = ArrayAdapter(this@Pantalla7, android.R.layout.simple_spinner_dropdown_item, mesesTexto)

            spinnersReady = false

            val listener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (!spinnersReady) return
                    lastCuenta = spinnerCuenta.selectedItem.toString()
                    lastAno    = anosRaw[spinnerAno.selectedItemPosition]
                    lastMes    = mesesRaw[spinnerMes.selectedItemPosition]
                    cargarLista()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            spinnerCuenta.onItemSelectedListener = listener
            spinnerAno.onItemSelectedListener    = listener
            spinnerMes.onItemSelectedListener    = listener

            spinnersReady = true
            cargarLista()
        }
    }

    private fun cargarLista() {
        lifecycleScope.launch {
            val filtrados = db.movimientoDao().obtenerFiltrados(lastCuenta, lastAno, lastMes, modoOrden)
            adapter.updateData(filtrados)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarLista()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            goHome()
        }
    }
}
