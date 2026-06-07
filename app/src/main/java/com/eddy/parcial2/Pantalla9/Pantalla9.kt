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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class Pantalla9 : AppCompatActivity() {

    private lateinit var binding: ActivityPantalla9Binding
    private lateinit var repositorio: ICuentaRepository
    private lateinit var adaptador: CuentaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPantalla9Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val barrasSistema = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(barrasSistema.left, barrasSistema.top, barrasSistema.right, barrasSistema.bottom)
            insets
        }

        val baseDatos = AppDatabase.getDatabase(this)
        repositorio = CuentaRepository(baseDatos.cuentaDao())

        configurarRecycler()

        binding.botonTopRegresar.setOnClickListener { irAlInicio() }

        binding.botonTopAgregar.setOnClickListener {
            // Abre Pantalla #10 se tiene que agregar logica de la pantalla 10 para agregar cuenta (por implementar)
            Toast.makeText(this, "Pantalla 10: Agregar cuenta", Toast.LENGTH_SHORT).show()
        }

        cargarCuentas()
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
                        // Abre Pantalla #10 se tiene que agregar logica de la pantalla 10 para modificar cuenta (por implementar)
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
    override fun onBackPressed() { irAlInicio() }
}
