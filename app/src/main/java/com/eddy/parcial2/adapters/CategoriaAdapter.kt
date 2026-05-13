package com.eddy.parcial2.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eddy.parcial2.activities.DetalleCategoriaActivity
import com.eddy.parcial2.databinding.ItemCategoriaBinding
import com.eddy.parcial2.models.CategoriaResumen

class CategoriaAdapter(
    private val lista: MutableList<CategoriaResumen>
) : RecyclerView.Adapter<CategoriaAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemCategoriaBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = ItemCategoriaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val categoria = lista[position]

        holder.binding.imgIcono.setImageResource(
            categoria.icono
        )

        holder.binding.txtNombre.text =
            categoria.nombre

        holder.binding.txtMovimientos.text =
            "(${categoria.movimientos} movimientos)"

        holder.binding.txtTotal.text =
            "$${categoria.total}"

        holder.binding.progreso.progress =
            categoria.porcentaje

        holder.itemView.setOnClickListener {

            val intent = Intent(
                holder.itemView.context,
                DetalleCategoriaActivity::class.java
            )

            intent.putExtra(
                "categoria",
                categoria.nombre
            )

            intent.putExtra(
                "total",
                categoria.total
            )

            holder.itemView.context.startActivity(intent)
        }
    }
}