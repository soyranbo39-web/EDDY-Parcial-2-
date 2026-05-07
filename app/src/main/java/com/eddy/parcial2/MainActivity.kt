package com.eddy.parcial2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eddy.parcial2.login.LoginActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PREFS = "session_prefs"
        private const val KEY_LOGGED = "is_logged"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Esta actividad actúa como un enrutador (Splash Screen)
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        
        if (prefs.getBoolean(KEY_LOGGED, false)) {
            // Si ya hay sesión, vamos al Home
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            // Si no, vamos al Login
            startActivity(Intent(this, LoginActivity::class.java))
        }
        
        // Cerramos esta actividad para que no quede en la pila
        finish()
    }
}
