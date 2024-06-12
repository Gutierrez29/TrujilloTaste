package com.uct.trujillotaste

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar los listeners para los botones
        val btnRegistrarse: Button = findViewById(R.id.btn_registrarse)
        val tvRegistrase: TextView = findViewById(R.id.tv_registrase)
        val btnIniciarSesion: Button = findViewById(R.id.btn_iniciar_sesion)
        val chkRecuerdame: CheckBox = findViewById(R.id.Recuerdame)

        val usuarioEditText: EditText = findViewById(R.id.usuario)
        val contraseñaEditText: EditText = findViewById(R.id.contraseña)

        // Verificar si el usuario está recordado
        val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val rememberedUser = sharedPref.getString("remembered_user", null)
        val rememberedPassword = sharedPref.getString("remembered_password", null)

        if (rememberedUser != null && rememberedPassword != null) {
            usuarioEditText.setText(rememberedUser)
            contraseñaEditText.setText(rememberedPassword)
            chkRecuerdame.isChecked = true
            iniciarSesion(rememberedUser, rememberedPassword)
        }

        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        tvRegistrase.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        btnIniciarSesion.setOnClickListener {
            val username = usuarioEditText.text.toString()
            val password = contraseñaEditText.text.toString()

            if (checkCredentials(username, password)) {
                if (chkRecuerdame.isChecked) {
                    val editor = sharedPref.edit()
                    editor.putString("remembered_user", username)
                    editor.putString("remembered_password", password)
                    editor.apply()
                } else {
                    val editor = sharedPref.edit()
                    editor.remove("remembered_user")
                    editor.remove("remembered_password")
                    editor.apply()
                }
                iniciarSesion(username, password)
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkCredentials(username: String, password: String): Boolean {
        val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val storedPassword = sharedPref.getString("${username}_password", null)
        return storedPassword == password
    }

    private fun iniciarSesion(username: String, password: String) {
        val intent = Intent(this, inicioActivity::class.java)
        startActivity(intent)
        finish()
    }
}