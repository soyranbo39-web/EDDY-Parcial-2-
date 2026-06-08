package com.eddy.parcial2.Pantalla9

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.databinding.DialogPantalla10Binding
import com.eddy.parcial2.Pantalla9.interfaces.ICuentaRepository
import com.eddy.parcial2.Pantalla9.models.Cuenta
import com.eddy.parcial2.Pantalla9.repository.CuentaRepository
import kotlinx.coroutines.launch

class Pantalla10 : DialogFragment() {

    private var _binding: DialogPantalla10Binding? = null
    private val binding get() = _binding!!

    private lateinit var repositorio: ICuentaRepository
    private var cuentaId: Int? = null

    private val iconos = listOf(
        "ic_card", "ic_wallet", "ic_house", "ic_gas", "ic_food", "ic_clothes"
    )

    companion object {
        fun newInstance(id: Int? = null): Pantalla10 {
            val frag = Pantalla10()
            val args = Bundle()
            if (id != null) args.putInt("id", id)
            frag.arguments = args
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cuentaId = arguments?.getInt("id").takeIf { it != 0 }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPantalla10Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val baseDatos = AppDatabase.getDatabase(requireContext())
        repositorio = CuentaRepository(baseDatos.cuentaDao())

        // Configurar spinner de iconos
        val adapterIconos = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            iconos
        )
        binding.spinnerIconos.adapter = adapterIconos

        // Actualizar vista previa al cambiar icono en el spinner
        binding.spinnerIconos.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    v: View?,
                    position: Int,
                    id: Long
                ) {
                    actualizarVistaPrevia(iconos[position])
                }
                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            }

        // Si hay id, modo modificar: precargar datos
        if (cuentaId != null) {
            binding.txtTituloDialog.text = "Modificar Cuenta"
            lifecycleScope.launch {
                repositorio.obtenerPorId(cuentaId!!)?.let { cuenta ->
                    binding.editNombre.setText(cuenta.nombre)
                    binding.editDescripcion.setText(cuenta.descripcion)
                    val posicion = iconos.indexOf(cuenta.icono)
                    if (posicion != -1) {
                        binding.spinnerIconos.setSelection(posicion)
                        actualizarVistaPrevia(cuenta.icono)
                    }
                }
            }
        }

        binding.btnCancelar.setOnClickListener { dismiss() }

        binding.btnGuardar.setOnClickListener {
            val nombre = binding.editNombre.text.toString().trim()
            val descripcion = binding.editDescripcion.text.toString().trim()
            val icono = binding.spinnerIconos.selectedItem.toString()

            if (nombre.isBlank() || descripcion.isBlank()) {
                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                if (cuentaId == null) {
                    repositorio.insertar(
                        Cuenta(nombre = nombre, descripcion = descripcion, icono = icono)
                    )
                } else {
                    repositorio.actualizar(
                        Cuenta(id = cuentaId!!, nombre = nombre, descripcion = descripcion, icono = icono)
                    )
                }
                (activity as? Pantalla9)?.cargarCuentas()
                dismiss()
            }
        }
    }

    private fun actualizarVistaPrevia(nombreIcono: String) {
        val contexto = requireContext()
        val recursoId = contexto.resources.getIdentifier(nombreIcono, "drawable", contexto.packageName)
        if (recursoId != 0) {
            binding.imgVistaPrevia.setImageResource(recursoId)
        } else {
            binding.imgVistaPrevia.setImageResource(android.R.drawable.ic_menu_help)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
