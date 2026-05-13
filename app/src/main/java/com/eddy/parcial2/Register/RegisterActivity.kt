package com.eddy.parcial2.registrar

import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eddy.parcial2.registrar.interfaces.IRegisterInteractor
import com.eddy.parcial2.registrar.model.RegisterData
import com.eddy.parcial2.data.AppDatabase
import com.eddy.parcial2.data.UserRepository
import com.eddy.parcial2.databinding.Activity2Binding
import com.eddy.parcial2.Login.model.LoginResult
import com.eddy.parcial2.registrar.objec.RegisterInteractor
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: Activity2Binding
    private lateinit var registerInteractor: IRegisterInteractor
    private var selectedAvatarId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(database.userDao())
        registerInteractor = RegisterInteractor(userRepository)

        setupAvatarSelection()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnGoBack.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val data = RegisterData(
                email = binding.etEmail.text.toString().trim(),
                username = binding.etUsername.text.toString().trim(),
                password = binding.etPassword.text.toString().trim(),
                confirmPassword = binding.etConfirmPassword.text.toString().trim(),
                avatarId = selectedAvatarId
            )

            val error = registerInteractor.validate(data)
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                when (val result = registerInteractor.register(data)) {
                    is LoginResult.Success -> {
                        Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is LoginResult.Error -> {
                        Toast.makeText(this@RegisterActivity, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupAvatarSelection() {
        val avatars = listOf(binding.ibAvatar1, binding.ibAvatar2, binding.ibAvatar3, binding.ibAvatar4)
        
        avatars.forEachIndexed { index, imageButton ->
            imageButton.setOnClickListener {
                selectedAvatarId = index + 1
                highlightSelectedAvatar(index)
            }
        }
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
