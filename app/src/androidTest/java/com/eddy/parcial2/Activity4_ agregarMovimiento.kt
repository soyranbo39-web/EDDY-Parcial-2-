package com.eddy.parcial2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eddy.parcial2.databinding.Activity4layoutBinding

class Activity4_agregarMovimiento : AppCompatActivity() {

    private lateinit var binding: Activity4layoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = Activity4layoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}