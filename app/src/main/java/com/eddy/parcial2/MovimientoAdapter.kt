package com.eddy.parcial2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovimientoAdapter(
    private val lista: List<MovimientoContenedor>
) : RecyclerView.Adapter<MovimientoAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textTitulo: TextView = view.findViewById(R.id.contenedorMovimientoTextoTitulo)
        val textValor: TextView = view.findViewById(R.id.contenedorMovimientoTextoValor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movimiento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movimiento = lista[position]
        holder.textTitulo.text = movimiento.titulo
        holder.textValor.text = movimiento.valor
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}