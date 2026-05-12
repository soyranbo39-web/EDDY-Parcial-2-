package com.eddy.parcial2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eddy.parcial2.databinding.Activity3PantalladeinicioBinding


class Categoria (var tipo: String, var nombre : String, var cantidad : Int )
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

    private lateinit var binding: Activity3PantalladeinicioBinding
    private lateinit var tipoMovimientoSp: Spinner
    private lateinit var ingresoBt : Button
    private lateinit var retiroBt : Button
    private lateinit var transferenciaBt : Button
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
        binding = Activity3PantalladeinicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tipoMovimientoSp = findViewById(R.id.tipoMovimeinto)
        val opcionesTipoMovimiento = arrayOf("Todas", "Debito", "Crédito", "Vales")
        val adapterTiposMovimeintosSp = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesTipoMovimiento)
        adapterTiposMovimeintosSp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tipoMovimientoSp.adapter = adapterTiposMovimeintosSp


        ingresoBt = findViewById(R.id.ingresoButton)
        ingresoBt.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //startActivity(Intent(this@Activity3_PantallaDeInicio, SegundaActivity::class.Kotlin))
            }
        })

        retiroBt = findViewById(R.id.retiroButton)
        retiroBt.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //startActivity(Intent(this@Activity3_PantallaDeInicio, SegundaActivity::class.Kotlin))
            }
        })

        transferenciaBt = findViewById(R.id.transferenciaButton)
        transferenciaBt.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //startActivity(Intent(this@Activity3_PantallaDeInicio, SegundaActivity::class.Kotlin))
            }
        })

        yearSp = findViewById(R.id.añoSpinnenr)
        val years = ArrayList<String>()
        val yearActual = 2026
        for (i in yearActual downTo 2010) {
            years.add(i.toString())
        }
        val adapterYear = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSp.adapter = adapterYear

        monthSp = findViewById(R.id.mesSpinner)
        val opcionesMes = arrayOf("Enero", "Febrero", "Marzo", "Abril", "Mayo"
        ,"Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
        val adapterMesSp = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesMes)
        adapterMesSp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSp.adapter = adapterMesSp

        ingresoTx = findViewById(R.id.ingreso)
        saldoAnteriorTx = findViewById(R.id.saldoAnterior)
        gastosTx = findViewById(R.id.gastos)
        saldoActualTx = findViewById(R.id.saldoActual)


        Categorias = mutableListOf(
            Categoria("Debito", "Comida", 1200),
            Categoria("Crédito", "Gasolina", 800),
            Categoria("Vales", "Despensa", 500)
        )
        categoriaRV = findViewById(R.id.categoriaRecycleView)
        categoriaRV.layoutManager = LinearLayoutManager(this)
        val adapter = CategoriaAdapter(Categorias)
        categoriaRV.adapter = adapter

    }
}