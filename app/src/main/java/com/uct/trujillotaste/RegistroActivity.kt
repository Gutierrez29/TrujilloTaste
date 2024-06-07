package com.uct.trujillotaste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar los listeners para los botones y TextView
        val btnRegistrarse: Button = findViewById(R.id.btn_registrase2)
        val tvVolverInicio: TextView = findViewById(R.id.volver_pll_inicio)

        btnRegistrarse.setOnClickListener {
            // Aquí puedes agregar la lógica de registro, como guardar los datos del usuario
            // Ejemplo: mostrar un mensaje de registro exitoso
            // Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
        }

        tvVolverInicio.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finaliza la actividad actual para que no esté en la pila de retroceso
        }
    }
}