package com.eddy.parcial2.FinanzasCompartidas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eddy.parcial2.R
import com.eddy.parcial2.FinanzasCompartidas.models.GastoCompartido
import com.eddy.parcial2.databinding.ActivityPantallaBBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class PantallaB : AppCompatActivity() {

    private lateinit var binding: ActivityPantallaBBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseDatabase.getInstance().reference

    private val gastos = mutableListOf<GastoCompartido>()
    private lateinit var adaptador: GastoAdapter
    private lateinit var grupoId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantallaBBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        grupoId = intent.getStringExtra("grupoId") ?: run { finish(); return }
        val grupoNombre = intent.getStringExtra("grupoNombre") ?: "Grupo"
        binding.txtNombreGrupo.text = grupoNombre

        adaptador = GastoAdapter(gastos)
        binding.recyclerGastos.layoutManager = LinearLayoutManager(this)
        binding.recyclerGastos.adapter = adaptador

        binding.btnRegresar.setOnClickListener { finish() }

        binding.btnAgregarGasto.setOnClickListener { mostrarDialogoAgregarGasto() }

        escucharGastos()
    }

    private fun escucharGastos() {
        db.child("gastos").child(grupoId)
            .orderByChild("timestamp")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val gasto = snapshot.getValue(GastoCompartido::class.java) ?: return
                    gastos.add(0, gasto) // cronológico inverso: más reciente arriba
                    adaptador.notifyItemInserted(0)
                    actualizarBalance()
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val gasto = snapshot.getValue(GastoCompartido::class.java) ?: return
                    val idx = gastos.indexOfFirst { it.id == gasto.id }
                    if (idx != -1) {
                        gastos[idx] = gasto
                        adaptador.notifyItemChanged(idx)
                        actualizarBalance()
                    }
                }
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun actualizarBalance() {
        val total = gastos.sumOf { it.monto }
        binding.txtBalanceGeneral.text = "Balance general: $${"%.2f".format(total)}"
    }

    private fun mostrarDialogoAgregarGasto() {
        val vista = layoutInflater.inflate(R.layout.dialog_agregar_gasto, null)
        AlertDialog.Builder(this)
            .setTitle("Agregar Gasto")
            .setView(vista)
            .setPositiveButton("Agregar") { _, _ ->
                val desc = vista.findViewById<TextInputEditText>(R.id.editDescGasto)
                    .text.toString().trim()
                val montoStr = vista.findViewById<TextInputEditText>(R.id.editMontoGasto)
                    .text.toString().trim()

                if (desc.isBlank() || montoStr.isBlank()) {
                    Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                val monto = montoStr.toDoubleOrNull() ?: run {
                    Toast.makeText(this, "Monto inválido", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                guardarGasto(desc, monto)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun guardarGasto(descripcion: String, monto: Double) {
        val uid = auth.currentUser?.uid ?: return
        val nombre = auth.currentUser?.email ?: "Usuario"
        val gastoRef = db.child("gastos").child(grupoId).push()
        val gasto = GastoCompartido(
            id = gastoRef.key ?: "",
            descripcion = descripcion,
            monto = monto,
            pagadoPorUid = uid,
            pagadoPorNombre = nombre,
            timestamp = System.currentTimeMillis()
        )
        gastoRef.setValue(gasto)
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar gasto", Toast.LENGTH_SHORT).show()
            }
    }

    // ── Adapter interno ──────────────────────────────────────────────────────
    inner class GastoAdapter(
        private val lista: List<GastoCompartido>
    ) : RecyclerView.Adapter<GastoAdapter.VH>() {

        inner class VH(view: View) : RecyclerView.ViewHolder(view) {
            val txtDescripcion: TextView = view.findViewById(R.id.txtDescGasto)
            val txtMonto: TextView = view.findViewById(R.id.txtMontoGasto)
            val txtPagadoPor: TextView = view.findViewById(R.id.txtPagadoPor)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gasto, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val gasto = lista[position]
            holder.txtDescripcion.text = gasto.descripcion
            holder.txtMonto.text = "$${"%.2f".format(gasto.monto)}"
            holder.txtPagadoPor.text = "Pagado por ${gasto.pagadoPorNombre}"
        }

        override fun getItemCount() = lista.size
    }
}
