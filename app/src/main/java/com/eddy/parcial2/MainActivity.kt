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

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    // Define constantes para las preferencias de sesión, incluyendo si el usuario está logueado y su correo electrónico

    companion object {
        private const val PREFS = "session_prefs"
        private const val KEY_LOGGED = "is_logged"
        private const val KEY_EMAIL = "user_email"
    }
    // En el método onCreate, se verifica si el usuario ya está logueado. Si es así, se redirige a HomeActivity. Si no, se muestra la pantalla de inicio de sesión y se configuran los botones para iniciar sesión o crear una cuenta
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        if (prefs.getBoolean(KEY_LOGGED, false)) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_pantalla_1)

        // Se inicializa el DatabaseHelper y se crea un usuario demo si no existe para facilitar las pruebas
        dbHelper = DatabaseHelper(this)
        dbHelper.createDemoUserIfNeeded()
        // Se obtienen las referencias a los elementos de la interfaz, como los campos de correo y contraseña, los botones de inicio de sesión y creación de cuenta, y el TextView para mostrar errores
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        val tvError = findViewById<TextView>(R.id.tvError)

        // Configura el botón de inicio de sesión para validar los campos, autenticar al usuario y manejar la sesión
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

            if (dbHelper.authenticate(correo, password)) {
                prefs.edit()
                    .putBoolean(KEY_LOGGED, true)
                    .putString(KEY_EMAIL, correo)
                    .apply()

                Toast.makeText(this, "Inicio de sesion correcto", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                tvError.text = "Correo o contrasena incorrectos"
                tvError.visibility = View.VISIBLE
            }
        }
        // Configura el botón de creación de cuenta para validar los campos, intentar crear un nuevo usuario y mostrar mensajes de éxito o error
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

            val creado = dbHelper.insertUser(correo, password)
            if (creado) {
                Toast.makeText(this, "Cuenta creada. Ahora puedes iniciar sesion.", Toast.LENGTH_LONG).show()
                etPassword.text.clear()
            } else {
                tvError.text = "Ese correo ya existe"
                tvError.visibility = View.VISIBLE
            }
        }
    }
    // Este método valida que el correo y la contraseña cumplan con los requisitos básicos, como no estar vacíos, tener un formato de correo válido y una longitud mínima para la contraseña
    private fun validarCampos(correo: String, password: String): String? {
        return when {
            correo.isEmpty() -> "El correo es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> "Correo invalido"
            password.isEmpty() -> "La contrasena es obligatoria"
            password.length < 6 -> "La contrasena debe tener al menos 6 caracteres"
            else -> null
        }
    }
}