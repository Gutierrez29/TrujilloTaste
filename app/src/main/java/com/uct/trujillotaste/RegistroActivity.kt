package com.uct.trujillotaste

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

        val nombreUsuario: EditText = findViewById(R.id.nombre_usuario)
        val crearUsuario: EditText = findViewById(R.id.crear_usuario)
        val contraseña: EditText = findViewById(R.id.contraseña)
        val numeroTelefono: EditText = findViewById(R.id.numero_telefono)

        btnRegistrarse.setOnClickListener {
            val nombre = nombreUsuario.text.toString()
            val usuario = crearUsuario.text.toString()
            val password = contraseña.text.toString()
            val telefono = numeroTelefono.text.toString()

            if (nombre.isNotEmpty() && usuario.isNotEmpty() && password.isNotEmpty() && telefono.isNotEmpty()) {
                val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("${usuario}_name", nombre)
                editor.putString("${usuario}_password", password)
                editor.putString("${usuario}_phone", telefono)
                editor.apply()

                Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()

                // Redirigir a MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        tvVolverInicio.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}