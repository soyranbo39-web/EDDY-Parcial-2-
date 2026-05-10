package com.eddy.parcial2.Login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eddy.parcial2.home.HomeActivity
import com.eddy.parcial2.R
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.data.UserRepository
import com.eddy.parcial2.Login.interfaces.ILoginInteractor
import com.eddy.parcial2.Login.model.LoginCredentials
import com.eddy.parcial2.Login.model.LoginResult
import com.eddy.parcial2.Login.`object`.LoginInteractor
import com.eddy.parcial2.registrar.RegisterActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var loginInteractor: ILoginInteractor

    companion object {
        private const val PREFS = "session_prefs"
        private const val KEY_LOGGED = "is_logged"
        private const val KEY_EMAIL = "user_email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_1)

        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)

        // Inicialización de componentes
        val database = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(database.userDao())

        // El Interactor recibe el repositorio
        loginInteractor = LoginInteractor(userRepository)

        lifecycleScope.launch {
            userRepository.createDemoUserIfNeeded()
        }

        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        val tvError = findViewById<TextView>(R.id.tvError)

        btnLogin.setOnClickListener {
            val credentials = LoginCredentials(etCorreo.text.toString().trim(), etPassword.text.toString().trim())

            val error = loginInteractor.validate(credentials)
            if (error != null) {
                tvError.text = error
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            tvError.visibility = View.GONE

            lifecycleScope.launch {
                when (val result = loginInteractor.login(credentials)) {
                    is LoginResult.Success -> {
                        prefs.edit().putBoolean(KEY_LOGGED, true).putString(KEY_EMAIL, credentials.email).apply()
                        Toast.makeText(this@LoginActivity, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    }
                    is LoginResult.Error -> {
                        tvError.text = result.message
                        tvError.visibility = View.VISIBLE
                    }
                }
            }
        }

        btnCrearCuenta.setOnClickListener {
            // Ahora este botón redirige a la pantalla de Registro (Actividad 2)
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
