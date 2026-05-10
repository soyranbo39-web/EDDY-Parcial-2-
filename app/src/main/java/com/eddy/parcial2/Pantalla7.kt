package com.eddy.parcial2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Pantalla7 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla7)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
        val recyclerView =
            findViewById<RecyclerView>(R.id.recyclerListaMovimientos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val lista = listOf(
            MovimientoContenedor("Netflix", "$15"),
            MovimientoContenedor("Groceries", "$42"),
            MovimientoContenedor("Electricity", "$80"),
            MovimientoContenedor("Gas", "$20"),
            MovimientoContenedor("Internet", "$60")
        )
        recyclerView.adapter = MovimientoAdapter(lista)
    }
}