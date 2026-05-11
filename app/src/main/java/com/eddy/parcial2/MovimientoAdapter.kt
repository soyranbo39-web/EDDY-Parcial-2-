package com.eddy.parcial2

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovimientoAdapter(
    private val lista: List<MovimientoContenedor>
) : RecyclerView.Adapter<MovimientoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textoCantidad: TextView = view.findViewById(R.id.textoCantidad)
        val textoCuenta: TextView = view.findViewById(R.id.textoCuenta)
        val textoCategoria: TextView = view.findViewById(R.id.textoCategoria)
        val textoFecha: TextView = view.findViewById(R.id.textoFecha)

        val iconoCuenta: ImageView = view.findViewById(R.id.iconoCuenta)
        val iconoTransferencia: ImageView = view.findViewById(R.id.iconoTipoTransferencia)
        val iconoCategoria: ImageView = view.findViewById(R.id.iconoCategoria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movimiento, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val movimiento = lista[position]

        holder.textoCantidad.text = movimiento.cantidad.toString()
        holder.textoCuenta.text = movimiento.nombreCuenta
        holder.textoCategoria.text = movimiento.categoria
        holder.textoFecha.text = holder.itemView.context.getString(
            R.string.fecha_formateada,
            movimiento.dia,
            movimiento.mes,
            movimiento.ano
        )

        holder.iconoCuenta.setImageResource(
            getCuentaIcon(movimiento.tipoCuenta)
        )

        holder.iconoCategoria.setImageResource(
            getCategoriaIcon(movimiento.categoria)
        )

        val color = when (movimiento.tipoTransferencia) {
            "Ingreso" -> Color.GREEN
            "Gasto" -> Color.RED
            "Transferencia" -> Color.YELLOW
            else -> Color.WHITE
        }

        holder.iconoTransferencia.setImageResource(
            getTransferenciaIcon(movimiento.tipoTransferencia)
        )

        holder.iconoTransferencia.setColorFilter(color)

        holder.itemView.setOnLongClickListener { view ->

            val popup = PopupMenu(view.context, view)
            popup.menu.add("Modificar Movimiento")

            popup.setOnMenuItemClickListener {
                true
            }

            popup.show()
            true
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }
    private fun getCuentaIcon(tipo: String): Int {
        return when (tipo) {
            "Efectivo" -> R.drawable.spinner_bg
            "TarjetaDebito" -> R.drawable.spinner_bg
            "TarjetaCredito" -> R.drawable.spinner_bg
            else -> R.drawable.spinner_bg
        }
    }
    private fun getTransferenciaIcon(tipo: String): Int {
        return when (tipo) {
            "Ingreso" -> android.R.drawable.arrow_down_float
            "Gasto" -> android.R.drawable.arrow_up_float
            "Transferencia" -> android.R.drawable.arrow_up_float
            else -> android.R.drawable.arrow_up_float
        }
    }
    private fun getCategoriaIcon(categoria: String): Int {
        return when (categoria) {
            "Comida" -> R.drawable.spinner_bg
            "Servicios" -> R.drawable.spinner_bg
            "Transporte" -> R.drawable.spinner_bg
            "Suscripciones" -> R.drawable.spinner_bg
            else -> R.drawable.spinner_bg
        }
    }
}