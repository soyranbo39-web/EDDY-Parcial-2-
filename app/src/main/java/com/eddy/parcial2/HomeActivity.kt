package com.eddy.parcial2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// Esta actividad muestra un mensaje de bienvenida al usuario autenticado y permite cerrar sesión
class HomeActivity : AppCompatActivity() {

    companion object {
        private const val PREFS = "session_prefs"
        private const val KEY_LOGGED = "is_logged"
        private const val KEY_EMAIL = "user_email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val tvUsuario = findViewById<TextView>(R.id.tvUsuario)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        val email = prefs.getString(KEY_EMAIL, "usuario")
        tvUsuario.text = "Bienvenido: $email"

        btnCerrarSesion.setOnClickListener {
            prefs.edit()
                .putBoolean(KEY_LOGGED, false)
                .remove(KEY_EMAIL)
                .apply()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}