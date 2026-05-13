package com.eddy.parcial2

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
import kotlinx.coroutines.launch

class Pantalla7 : AppCompatActivity() {

    private var modoOrden = "fecha"

    private var lastCuenta = "Cuenta"
    private var lastAno = -1
    private var lastMes = -1

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla7)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerListaMovimientos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val spinnerCuenta = findViewById<Spinner>(R.id.spinnerSortCuenta)
        val spinnerAno = findViewById<Spinner>(R.id.spinnerSortAno)
        val spinnerMes = findViewById<Spinner>(R.id.spinnerSortMes)

        val sortButton = findViewById<View>(R.id.botonTopSort)

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

                cargarFiltrosYLista()
                true
            }

            popup.show()
        }

        cargarFiltrosYLista()
    }

    private fun cargarFiltrosYLista() {

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerListaMovimientos)
        val spinnerCuenta = findViewById<Spinner>(R.id.spinnerSortCuenta)
        val spinnerAno = findViewById<Spinner>(R.id.spinnerSortAno)
        val spinnerMes = findViewById<Spinner>(R.id.spinnerSortMes)

        lifecycleScope.launch {

            var cuentas = listOf("Cuenta") + db.movimientoDao().obtenerCuentas()
            var anos = listOf(-1) + db.movimientoDao().obtenerAnos()
            var meses = listOf(-1) + db.movimientoDao().obtenerMeses()

            val anosTexto = anos.map { if (it == -1) "Año" else it.toString() }
            val mesesTexto = meses.map { if (it == -1) "Mes" else it.toString() }

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

            val listener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    lastCuenta = spinnerCuenta.selectedItem.toString()
                    lastAno = anos[spinnerAno.selectedItemPosition]
                    lastMes = meses[spinnerMes.selectedItemPosition]

                    cargarLista()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            spinnerCuenta.onItemSelectedListener = listener
            spinnerAno.onItemSelectedListener = listener
            spinnerMes.onItemSelectedListener = listener

            cargarLista()
        }
    }

    private fun cargarLista() {

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerListaMovimientos)

        lifecycleScope.launch {

            val filtrados = db.movimientoDao().obtenerFiltrados(
                lastCuenta,
                lastAno,
                lastMes,
                modoOrden
            )

            recyclerView.adapter = MovimientoAdapter(filtrados)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarFiltrosYLista()
    }
}