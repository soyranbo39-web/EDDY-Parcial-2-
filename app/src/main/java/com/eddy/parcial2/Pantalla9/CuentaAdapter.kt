package com.eddy.parcial2.Pantalla9

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eddy.parcial2.databinding.ItemCuentaBinding
import com.eddy.parcial2.Pantalla9.models.Cuenta

class CuentaAdapter(
    private var lista: MutableList<Cuenta>,
    private val alHacerClic: (Cuenta) -> Unit
) : RecyclerView.Adapter<CuentaAdapter.ViewHolder>() {

    init { setHasStableIds(true) }

    override fun getItemId(posicion: Int): Long = lista[posicion].id.toLong()

    class ViewHolder(val enlace: ItemCuentaBinding) : RecyclerView.ViewHolder(enlace.root)

    override fun onCreateViewHolder(padre: ViewGroup, tipo: Int): ViewHolder {
        val enlace = ItemCuentaBinding.inflate(
            LayoutInflater.from(padre.context), padre, false
        )
        return ViewHolder(enlace)
    }

    override fun onBindViewHolder(holder: ViewHolder, posicion: Int) {
        val cuenta = lista[posicion]
        holder.enlace.txtNombre.text = cuenta.nombre
        holder.enlace.txtDescripcion.text = cuenta.descripcion

        val contexto = holder.itemView.context
        val recursoId = contexto.resources.getIdentifier(cuenta.icono, "drawable", contexto.packageName)
        if (recursoId != 0) {
            holder.enlace.imgIcono.setImageResource(recursoId)
        } else {
            holder.enlace.imgIcono.setImageResource(android.R.drawable.ic_menu_help)
        }

        holder.itemView.setOnClickListener { alHacerClic(cuenta) }
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarDatos(nuevaLista: List<Cuenta>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}
