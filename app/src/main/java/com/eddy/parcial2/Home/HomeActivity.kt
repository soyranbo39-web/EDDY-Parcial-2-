package com.eddy.parcial2.Home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import com.eddy.parcial2.R
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.data.UserRepository
import com.eddy.parcial2.databinding.ActivityHomeBinding
import com.eddy.parcial2.Home.interfaces.IHomeInteractor
import com.eddy.parcial2.Home.`object`.HomeInteractor
import com.eddy.parcial2.Login.LoginActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import androidx.core.content.edit
import com.eddy.parcial2.Pantalla7

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeInteractor: IHomeInteractor
    
    companion object {
        private const val PREFS = "session_prefs"
        private const val KEY_LOGGED = "is_logged"
        private const val KEY_EMAIL = "user_email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(database.userDao())
        homeInteractor = HomeInteractor(userRepository)

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.registrar, R.string.registrar
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        loadUserData()
    }

    private fun loadUserData() {
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        val email = prefs.getString(KEY_EMAIL, null) ?: return

        if (binding.navView.headerCount == 0) {
            return
        }

        val headerView = binding.navView.getHeaderView(0)
        val ivAvatar = headerView.findViewById<ImageView>(R.id.ivUserAvatar)
        val tvUsername = headerView.findViewById<TextView>(R.id.tvHeaderUsername)
        val tvEmail = headerView.findViewById<TextView>(R.id.tvHeaderEmail)

        lifecycleScope.launch {
            runCatching {
                homeInteractor.getUserData(email)
            }.onSuccess { userData ->
                userData?.let {
                    tvUsername.text = it.username
                    tvEmail.text = it.email
                    // Imagen Avatar
                    val avatarRes = when (it.avatarId) {
                        1 -> R.drawable.abrahan1
                        2 -> R.drawable.abraham2
                        3 -> R.drawable.abraham3
                        4 -> R.drawable.abraham4
                        else -> R.drawable.ic_launcher_foreground
                    }
                    ivAvatar.setImageResource(avatarRes)
                }
            }.onFailure {
                Toast.makeText(this@HomeActivity, "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
                R.id.nav_inicio ->
                {
                    startActivity(Intent(this, com.eddy.parcial2.activities.Activity3PantallaDeInicio::class.java))
                }

            R.id.nav_movimientos -> {
                startActivity(Intent(this, Pantalla7::class.java))
            }

            R.id.nav_cuentas -> {
                // pantalla de cuentas, no se cual es xd
                  Toast.makeText(this, "Cuentas", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_categorias -> {
                startActivity(Intent(this, com.eddy.parcial2.activities.ReporteCategoriasActivity::class.java))
            }

            R.id.nav_ayuda -> {
                // pantalla de ayuda
                 Toast.makeText(this, "ño quiello ayudate :(", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_acerca_de -> {
                // pantalla acerca de
                 Toast.makeText(this, "Acerca de", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_logout -> logout()
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        prefs.edit {
            putBoolean(KEY_LOGGED, false)
                .remove(KEY_EMAIL)
        }

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
