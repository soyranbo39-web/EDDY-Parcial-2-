package com.eddy.parcial2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.eddy.parcial2.databinding.ActivityMainBinding
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager

data class Categoria (var tipo: String, var nombre : String, var cantidad : Int )
class CategoriaAdapter(private val lista: List<Categoria>) :

    RecyclerView.Adapter<CategoriaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.nombreCategoria)
        val tipo: TextView = view.findViewById(R.id.tipoCategoria)
        val cantidad: TextView = view.findViewById(R.id.cantidadCategoria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.categoria_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoria = lista[position]
        holder.nombre.text = categoria.nombre
        holder.tipo.text = categoria.tipo
        holder.cantidad.text = categoria.cantidad.toString()
    }
}
class Activity3_PantallaDeInicio : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tipoMovimientoSp: Spinner
    private lateinit var ingresoBt : Button
    private lateinit var retiroBt : Button
    private lateinit var tranferenciaBt : Button
    private lateinit var yearSp: Spinner
    private lateinit var monthSp: Spinner

    private lateinit var ingresoTx: TextView
    private lateinit var saldoAnteriorTx: TextView
    private lateinit var gastosTx: TextView
    private lateinit var saldoActualTx: TextView
    private lateinit var categoriaRV: RecyclerView

    private lateinit var Categorias: MutableList<Categoria>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tipoMovimientoSp = findViewById(R.id.tipoMovimeinto)
        val opcionesTipoMovimiento = arrayOf("Todas", "Debito", "Crédito", "Vales")
        val adapterTiposMovimeintosSp = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesTipoMovimiento)
        adapterTiposMovimeintosSp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tipoMovimientoSp.adapter = adapterTiposMovimeintosSp


        ingresoBt = findViewById(R.id.ingresoButton)
        retiroBt = findViewById(R.id.retiroButton)
        tranferenciaBt = findViewById(R.id.transefenciaButton)
        yearSp = findViewById(R.id.añoSpinnenr)
        monthSp = findViewById(R.id.mesSpinner)
        ingresoTx = findViewById(R.id.ingreso)
        saldoAnteriorTx = findViewById(R.id.saldoAnterior)
        gastosTx = findViewById(R.id.gastos)
        saldoActualTx = findViewById(R.id.saldoActual)

        categoriaRV = findViewById(R.id.categoriaRecycleView)
        categoriaRV.layoutManager = LinearLayoutManager(this)
        val adapter = CategoriaAdapter(Categorias)
        categoriaRV.adapter = adapter


    }
}