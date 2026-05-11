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

        val db = AppDatabase.getDatabase(this)

        var cuentas = listOf<String>()
        var anos = listOf<Int>()
        var meses = listOf<Int>()

        fun actualizarLista() {

            val cuentaSeleccionada = spinnerCuenta.selectedItem.toString()
            val anoSeleccionado = anos[spinnerAno.selectedItemPosition]
            val mesSeleccionado = meses[spinnerMes.selectedItemPosition]

            lifecycleScope.launch {

                val filtrados = db.movimientoDao().obtenerFiltrados(
                    cuentaSeleccionada,
                    anoSeleccionado,
                    mesSeleccionado,
                    modoOrden
                )

                recyclerView.adapter = MovimientoAdapter(filtrados)
            }
        }

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

                actualizarLista()
                true
            }

            popup.show()
        }

        lifecycleScope.launch {

            cuentas = mutableListOf("Cuenta")
            cuentas = cuentas + db.movimientoDao().obtenerCuentas()

            anos = mutableListOf(-1) + db.movimientoDao().obtenerAnos()
            meses = mutableListOf(-1) + db.movimientoDao().obtenerMeses()

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
                    actualizarLista()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            spinnerCuenta.onItemSelectedListener = listener
            spinnerAno.onItemSelectedListener = listener
            spinnerMes.onItemSelectedListener = listener

            actualizarLista()
        }
    }
}