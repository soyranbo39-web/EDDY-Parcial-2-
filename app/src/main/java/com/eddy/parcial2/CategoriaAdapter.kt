package com.eddy.parcial2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eddy.parcial2.models.Categoria

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