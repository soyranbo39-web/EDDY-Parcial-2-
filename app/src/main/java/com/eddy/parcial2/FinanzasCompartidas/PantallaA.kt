package com.eddy.parcial2.FinanzasCompartidas

import android.content.Intent
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
import com.eddy.parcial2.FinanzasCompartidas.models.Grupo
import com.eddy.parcial2.databinding.ActivityPantallaABinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class PantallaA : AppCompatActivity() {

    private lateinit var binding: ActivityPantallaABinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseDatabase.getInstance().reference

    private val grupos = mutableListOf<Grupo>()
    private lateinit var adaptador: GrupoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantallaABinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        autenticarSiEsNecesario()

        adaptador = GrupoAdapter(grupos) { grupo ->
            val intent = Intent(this, PantallaB::class.java)
            intent.putExtra("grupoId", grupo.id)
            intent.putExtra("grupoNombre", grupo.nombre)
            startActivity(intent)
        }
        binding.recyclerGrupos.layoutManager = LinearLayoutManager(this)
        binding.recyclerGrupos.adapter = adaptador

        binding.btnRegresar.setOnClickListener { finish() }

        binding.btnCrearGrupo.setOnClickListener { mostrarDialogoCrear() }

        binding.btnUnirseGrupo.setOnClickListener { mostrarDialogoUnirse() }
    }

    private fun autenticarSiEsNecesario() {
        if (auth.currentUser == null) {
            val prefs = getSharedPreferences("session_prefs", MODE_PRIVATE)
            val email = prefs.getString("user_email", "") ?: ""
            val password = "firebase_session_pass"

            auth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { escucharGrupos() }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error de autenticación: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnSuccessListener { escucharGrupos() }
        } else {
            escucharGrupos()
        }
    }

    private fun escucharGrupos() {
        val uid = auth.currentUser?.uid ?: return
        db.child("grupos").orderByChild("miembros/$uid").equalTo(true)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    grupos.clear()
                    for (child in snapshot.children) {
                        val grupo = child.getValue(Grupo::class.java) ?: continue
                        grupos.add(grupo)
                    }
                    adaptador.notifyDataSetChanged()
                    binding.txtSinGrupos.visibility =
                        if (grupos.isEmpty()) View.VISIBLE else View.GONE
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun mostrarDialogoCrear() {
        val vista = layoutInflater.inflate(R.layout.dialog_crear_grupo, null)
        AlertDialog.Builder(this)
            .setTitle("Crear Grupo")
            .setView(vista)
            .setPositiveButton("Crear") { _, _ ->
                val nombre = vista.findViewById<TextInputEditText>(R.id.editNombreGrupo)
                    .text.toString().trim()
                if (nombre.isBlank()) {
                    Toast.makeText(this, "Ingresa un nombre", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                crearGrupo(nombre)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun crearGrupo(nombre: String) {
        val uid = auth.currentUser?.uid ?: return
        val codigo = UUID.randomUUID().toString().replace("-", "").take(8).uppercase()
        val grupoRef = db.child("grupos").push()
        val grupo = Grupo(
            id = grupoRef.key ?: "",
            nombre = nombre,
            codigo = codigo,
            miembros = mapOf(uid to true)
        )
        grupoRef.setValue(grupo)
            .addOnSuccessListener {
                AlertDialog.Builder(this)
                    .setTitle("Grupo creado")
                    .setMessage("Comparte este código con tus amigos:\n\n$codigo")
                    .setPositiveButton("Aceptar", null)
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al crear grupo", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarDialogoUnirse() {
        val vista = layoutInflater.inflate(R.layout.dialog_unirse_grupo, null)
        AlertDialog.Builder(this)
            .setTitle("Unirse a Grupo")
            .setView(vista)
            .setPositiveButton("Unirse") { _, _ ->
                val codigo = vista.findViewById<TextInputEditText>(R.id.editCodigoGrupo)
                    .text.toString().trim().uppercase()
                if (codigo.isBlank()) {
                    Toast.makeText(this, "Ingresa el código", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                unirseAGrupo(codigo)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun unirseAGrupo(codigo: String) {
        val uid = auth.currentUser?.uid ?: return
        db.child("grupos").orderByChild("codigo").equalTo(codigo)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        Toast.makeText(this@PantallaA, "Código no encontrado", Toast.LENGTH_SHORT).show()
                        return
                    }
                    val grupoSnap = snapshot.children.first()
                    grupoSnap.ref.child("miembros").child(uid).setValue(true)
                        .addOnSuccessListener {
                            Toast.makeText(this@PantallaA, "Te uniste al grupo", Toast.LENGTH_SHORT).show()
                        }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    // ── Adapter interno ──────────────────────────────────────────────────────
    inner class GrupoAdapter(
        private val lista: List<Grupo>,
        private val alHacerClic: (Grupo) -> Unit
    ) : RecyclerView.Adapter<GrupoAdapter.VH>() {

        inner class VH(view: View) : RecyclerView.ViewHolder(view) {
            val txtNombre: TextView = view.findViewById(R.id.txtNombreGrupo)
            val txtCodigo: TextView = view.findViewById(R.id.txtCodigoGrupo)
            val txtMiembros: TextView = view.findViewById(R.id.txtMiembros)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_grupo, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val grupo = lista[position]
            holder.txtNombre.text = grupo.nombre
            holder.txtCodigo.text = "Código: ${grupo.codigo}"
            holder.txtMiembros.text = "${grupo.miembros.size} miembro(s)"
            holder.itemView.setOnClickListener { alHacerClic(grupo) }
        }

        override fun getItemCount() = lista.size
    }
}
