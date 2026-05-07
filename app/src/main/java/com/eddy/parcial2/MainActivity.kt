package com.eddy.parcial2

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.data.UserRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var userRepository: UserRepository

    companion object {
        private const val PREFS = "session_prefs"
        private const val KEY_LOGGED = "is_logged"
        private const val KEY_EMAIL = "user_email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        if (prefs.getBoolean(KEY_LOGGED, false)) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_pantalla_1)

        // Inicializar Room y el Repositorio
        val database = AppDatabase.getDatabase(this)
        userRepository = UserRepository(database.userDao())

        // Crear usuario demo en una corrutina
        lifecycleScope.launch {
            userRepository.createDemoUserIfNeeded()
        }

        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        val tvError = findViewById<TextView>(R.id.tvError)

        btnLogin.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val password = etPassword.text.toString().trim()

            val error = validarCampos(correo, password)
            if (error != null) {
                tvError.text = error
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            tvError.visibility = View.GONE

            lifecycleScope.launch {
                if (userRepository.authenticate(correo, password)) {
                    prefs.edit()
                        .putBoolean(KEY_LOGGED, true)
                        .putString(KEY_EMAIL, correo)
                        .apply()

                    Toast.makeText(this@MainActivity, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                    finish()
                } else {
                    tvError.text = "Correo o contraseña incorrectos"
                    tvError.visibility = View.VISIBLE
                }
            }
        }

        btnCrearCuenta.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val password = etPassword.text.toString().trim()

            val error = validarCampos(correo, password)
            if (error != null) {
                tvError.text = error
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            tvError.visibility = View.GONE

            lifecycleScope.launch {
                val creado = userRepository.insertUser(correo, password)
                if (creado) {
                    Toast.makeText(this@MainActivity, "Cuenta creada. Ahora puedes iniciar sesión.", Toast.LENGTH_LONG).show()
                    etPassword.text.clear()
                } else {
                    tvError.text = "Ese correo ya existe"
                    tvError.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun validarCampos(correo: String, password: String): String? {
        return when {
            correo.isEmpty() -> "El correo es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> "Correo inválido"
            password.isEmpty() -> "La contraseña es obligatoria"
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }
}
