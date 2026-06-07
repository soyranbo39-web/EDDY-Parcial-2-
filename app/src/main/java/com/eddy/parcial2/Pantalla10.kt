package com.eddy.parcial2

import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.databinding.ActivityPantalla10Binding
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class Pantalla10 : AppCompatActivity() {

    private lateinit var binding: ActivityPantalla10Binding
    private lateinit var db: AppDatabase

    private var emailUsuarioLogueado: String = "ejemplo@correo.com"

    private var avatarSeleccionadoId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPantalla10Binding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(applicationContext)

        setupAvatarSelection()

        binding.btnModificar.setOnClickListener {
            val nuevoUsername = binding.editUsername.editText?.text.toString().trim()

            if (nuevoUsername.isEmpty()) {
                binding.editUsername.error = "El nombre no puede estar vacío"
            } else {
                binding.editUsername.error = null

                lifecycleScope.launch {
                    try {
                        val filasAfectadas = withContext(Dispatchers.IO) {
                            db.userDao().updateNameAndAvatarByEmail(
                                email = emailUsuarioLogueado,
                                nuevoNombre = nuevoUsername,
                                nuevoAvatarId = avatarSeleccionadoId
                            )
                        }

                        if (filasAfectadas > 0) {
                            Toast.makeText(this@Pantalla10, "Usuario actualizado con éxito", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@Pantalla10, "Error: No se encontró el usuario", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(this@Pantalla10, "Error al actualizar: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setupAvatarSelection() {
        val avatars = listOf(binding.ibAvatar1, binding.ibAvatar2, binding.ibAvatar3, binding.ibAvatar4)

        avatars.forEachIndexed { index, imageButton ->
            imageButton.setOnClickListener {
                avatarSeleccionadoId = index + 1
                highlightSelectedAvatar(index)
            }
        }

        highlightSelectedAvatar(0)
    }

    private fun highlightSelectedAvatar(selectedIndex: Int) {
        val avatars = listOf(binding.ibAvatar1, binding.ibAvatar2, binding.ibAvatar3, binding.ibAvatar4)
        avatars.forEachIndexed { index, imageView ->
            if (index == selectedIndex) {
                imageView.alpha = 1.0f
                imageView.strokeWidth = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics
                )
            } else {
                imageView.alpha = 0.6f
                imageView.strokeWidth = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 1f, resources.displayMetrics
                )
            }
        }
    }
}