package com.eddy.parcial2

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.databinding.DialogPantalla12Binding
import com.eddy.parcial2.models.Categoria
import kotlinx.coroutines.launch

class Pantalla12 : DialogFragment() {

    private var _binding: DialogPantalla12Binding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase
    private var categoriaId: Int? = null

    private val iconos = listOf(
        "ic_card", "ic_clothes", "ic_food", "ic_gas", "ic_house", "ic_wallet"
    )

    companion object {
        fun newInstance(id: Int? = null): Pantalla12 {
            val frag = Pantalla12()
            val args = Bundle()
            if (id != null) args.putInt("id", id)
            frag.arguments = args
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoriaId = arguments?.getInt("id")
        if (categoriaId == 0) categoriaId = null // Bundle default value
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogPantalla12Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getDatabase(requireContext())

        // Configurar Spinner de iconos
        val adapterIconos = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, iconos)
        binding.spinnerIconos.adapter = adapterIconos

        if (categoriaId != null) {
            binding.txtTituloDialog.text = "Modificar Categoría"
            lifecycleScope.launch {
                val cat = db.categoriaDao().obtenerPorId(categoriaId!!)
                cat?.let {
                    binding.editNombre.setText(it.nombre)
                    binding.editDescripcion.setText(it.descripcion)
                    val pos = iconos.indexOf(it.icono)
                    if (pos != -1) binding.spinnerIconos.setSelection(pos)
                }
            }
        }

        binding.btnCancelar.setOnClickListener { dismiss() }

        binding.btnGuardar.setOnClickListener {
            val nombre = binding.editNombre.text.toString()
            val desc = binding.editDescripcion.text.toString()
            val icono = binding.spinnerIconos.selectedItem.toString()

            if (nombre.isBlank() || desc.isBlank()) {
                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                if (categoriaId == null) {
                    db.categoriaDao().insertar(Categoria(nombre = nombre, descripcion = desc, icono = icono))
                } else {
                    db.categoriaDao().actualizar(Categoria(id = categoriaId!!, nombre = nombre, descripcion = desc, icono = icono))
                }
                (activity as? Pantalla11)?.cargarCategorias()
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
