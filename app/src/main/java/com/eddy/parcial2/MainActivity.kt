package com.eddy.parcial2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eddy.parcial2.Home.HomeActivity
import com.eddy.parcial2.Login.LoginActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PREFS = "session_prefs"
        private const val KEY_LOGGED = "is_logged"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  enrutador
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)

        if (prefs.getBoolean(KEY_LOGGED, false)) {
            //  Home
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            //  Login
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Cerramos esta actividad para que no quede en la pila
        finish()
    }
}