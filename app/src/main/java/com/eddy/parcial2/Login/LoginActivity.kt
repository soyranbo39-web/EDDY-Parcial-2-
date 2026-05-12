package com.eddy.parcial2.Login

import android.content.Intent
import android.os.Bundle
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
import com.eddy.parcial2.databinding.ActivityPantalla1Binding
import com.eddy.parcial2.registrar.RegisterActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPantalla1Binding
    private lateinit var loginInteractor: ILoginInteractor

    companion object {
        private const val PREFS = "session_prefs"
        private const val KEY_LOGGED = "is_logged"
        private const val KEY_EMAIL = "user_email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantalla1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)

        // Inicialización de componentes
        val database = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(database.userDao())

        // El Interactor recibe el repositorio
        loginInteractor = LoginInteractor(userRepository)

        lifecycleScope.launch {
            userRepository.createDemoUserIfNeeded()
        }

        binding.btnLogin.setOnClickListener {
            val credentials = LoginCredentials(
                binding.etCorreo.text.toString().trim(),
                binding.etPassword.text.toString().trim()
            )

            val error = loginInteractor.validate(credentials)
            if (error != null) {
                binding.tvError.text = error
                binding.tvError.visibility = android.view.View.VISIBLE
                return@setOnClickListener
            }

            binding.tvError.visibility = android.view.View.GONE

            lifecycleScope.launch {
                when (val result = loginInteractor.login(credentials)) {
                    is LoginResult.Success -> {
                        prefs.edit().putBoolean(KEY_LOGGED, true).putString(KEY_EMAIL, credentials.email).apply()
                        Toast.makeText(this@LoginActivity, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    }
                    is LoginResult.Error -> {
                        binding.tvError.text = result.message
                        binding.tvError.visibility = android.view.View.VISIBLE
                    }
                }
            }
        }

        binding.btnCrearCuenta.setOnClickListener {

            binding.etCorreo.text?.clear()
            binding.etPassword.text?.clear()

            binding.tvError.visibility = android.view.View.GONE

            //  Actividad 2
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
