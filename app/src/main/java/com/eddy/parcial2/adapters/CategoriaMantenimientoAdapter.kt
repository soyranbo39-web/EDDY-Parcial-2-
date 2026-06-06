package com.eddy.parcial2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eddy.parcial2.databinding.ItemCategoriaMantenimientoBinding
import com.eddy.parcial2.models.Categoria

class CategoriaMantenimientoAdapter(
    private var lista: MutableList<Categoria>,
    private val onItemClick: (Categoria) -> Unit
) : RecyclerView.Adapter<CategoriaMantenimientoAdapter.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = lista[position].id.toLong()

    class ViewHolder(val binding: ItemCategoriaMantenimientoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoriaMantenimientoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cat = lista[position]
        holder.binding.txtNombre.text = cat.nombre
        holder.binding.txtDescripcion.text = cat.descripcion
        
        // Cargar icono desde recursos
        val context = holder.itemView.context
        val resId = context.resources.getIdentifier(cat.icono, "drawable", context.packageName)
        if (resId != 0) {
            holder.binding.imgIcono.setImageResource(resId)
        } else {
            holder.binding.imgIcono.setImageResource(android.R.drawable.ic_menu_help)
        }

        holder.itemView.setOnClickListener { onItemClick(cat) }
    }

    override fun getItemCount(): Int = lista.size

    fun updateData(nuevaLista: List<Categoria>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}
