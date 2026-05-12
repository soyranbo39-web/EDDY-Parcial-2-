package com.eddy.parcial2.adapters

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.eddy.parcial2.R
import com.eddy.parcial2.databinding.ItemMovimientoBinding
import com.eddy.parcial2.models.Movimiento
import com.google.android.material.snackbar.Snackbar

class MovimientoAdapter(
    private val lista: MutableList<Movimiento>
) : RecyclerView.Adapter<MovimientoAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemMovimientoBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = ItemMovimientoBinding.inflate(
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

        val movimiento = lista[position]

        holder.binding.imgCuenta.setImageResource(
            movimiento.iconoCuenta
        )

        holder.binding.txtCuenta.text =
            movimiento.cuenta

        holder.binding.txtFecha.text =
            movimiento.fecha

        holder.binding.txtDescripcion.text =
            movimiento.descripcion

        holder.binding.txtCantidad.text =
            "$${movimiento.cantidad}"

        holder.itemView.setOnClickListener {

            val popup = PopupMenu(
                holder.itemView.context,
                holder.itemView
            )

            MenuInflater(holder.itemView.context)
                .inflate(
                    R.menu.menu_movimiento,
                    popup.menu
                )

            popup.setOnMenuItemClickListener {

                when (it.itemId) {

                    R.id.menuModificar -> {

                        true
                    }

                    R.id.menuEliminar -> {

                        val eliminado = lista[position]

                        lista.removeAt(position)

                        notifyItemRemoved(position)

                        Snackbar.make(
                            holder.itemView,
                            "Movimiento eliminado",
                            Snackbar.LENGTH_LONG
                        ).setAction("Deshacer") {

                            lista.add(
                                position,
                                eliminado
                            )

                            notifyItemInserted(position)

                        }.show()

                        true
                    }

                    else -> false
                }
            }

            popup.show()
        }
    }
}