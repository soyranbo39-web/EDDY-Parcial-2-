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
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.Home.HomeActivity
import kotlinx.coroutines.launch

class Pantalla7 : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla7)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerListaMovimientos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = MovimientoAdapter(mutableListOf())
        recyclerView.adapter = adapter

        spinnerCuenta = findViewById(R.id.spinnerSortCuenta)
        spinnerAno = findViewById(R.id.spinnerSortAno)
        spinnerMes = findViewById(R.id.spinnerSortMes)

        val sortButton = findViewById<View>(R.id.botonTopSort)
        val backButton = findViewById<View>(R.id.botonTopRegresar)

        db = AppDatabase.getDatabase(this)

        sortButton.setOnClickListener {
            val popup = PopupMenu(this, sortButton)

            popup.menu.add(Menu.NONE, 1, 1, "Fecha")
            popup.menu.add(Menu.NONE, 2, 2, "Cuenta")
            popup.menu.add(Menu.NONE, 3, 3, "Cantidad")

            popup.setOnMenuItemClickListener {
                modoOrden = when (it.itemId) {
                    2 -> "cuenta"
                    3 -> "cantidad"
                    else -> "fecha"
                }
                cargarLista()
                true
            }

            popup.show()
        }

        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        cargarFiltros()
    }

    private fun cargarFiltros() {
        lifecycleScope.launch {

            val cuentas = listOf("Cuenta") + db.movimientoDao().obtenerCuentas()
            val anosRaw = listOf(-1) + db.movimientoDao().obtenerAnos()
            val mesesRaw = listOf(-1) + db.movimientoDao().obtenerMeses()

            val anosTexto = anosRaw.map { if (it == -1) "Año" else it.toString() }
            val mesesTexto = mesesRaw.map { if (it == -1) "Mes" else it.toString() }

            spinnerCuenta.adapter = ArrayAdapter(
                this@Pantalla7,
                android.R.layout.simple_spinner_dropdown_item,
                cuentas
            )

            spinnerAno.adapter = ArrayAdapter(
                this@Pantalla7,
                android.R.layout.simple_spinner_dropdown_item,
                anosTexto
            )

            spinnerMes.adapter = ArrayAdapter(
                this@Pantalla7,
                android.R.layout.simple_spinner_dropdown_item,
                mesesTexto
            )

            spinnersReady = false

            val listener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    if (!spinnersReady) return

                    lastCuenta = spinnerCuenta.selectedItem.toString()
                    lastAno = anosRaw[spinnerAno.selectedItemPosition]
                    lastMes = mesesRaw[spinnerMes.selectedItemPosition]

                    cargarLista()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            spinnerCuenta.onItemSelectedListener = listener
            spinnerAno.onItemSelectedListener = listener
            spinnerMes.onItemSelectedListener = listener

            spinnersReady = true
            cargarLista()
        }
    }

    private fun cargarLista() {
        lifecycleScope.launch {

            val filtrados = db.movimientoDao().obtenerFiltrados(
                lastCuenta,
                lastAno,
                lastMes,
                modoOrden
            )

            adapter.updateData(filtrados)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarLista()
    }

    override fun onBackPressed() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }
}