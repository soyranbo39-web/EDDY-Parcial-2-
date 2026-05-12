package com.eddy.parcial2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eddy.parcial2.activities.ReporteCategoriasActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(
            Intent(this, ReporteCategoriasActivity::class.java)
        )

        finish()
    }
}