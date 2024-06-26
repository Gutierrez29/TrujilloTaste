package com.uct.trujillotaste

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var correoEditText: EditText
    private lateinit var contraseñaEditText: EditText
    private lateinit var recuerdameCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar Firebase Analytics
        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle().apply {
            putString("message", "integracion de firebase completa")
        }
        analytics.logEvent("IniScreen", bundle)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        // Verificar si el usuario ha marcado "Recuerdame" y redirigir si es necesario
        if (sharedPreferences.getBoolean("recuerdameChecked", false)) {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            finish() // Terminar MainActivity para que el usuario no pueda volver usando el botón de atrás
            return
        }

        // Configurar los listeners para los botones y vistas
        val btnIniciarSesion: Button = findViewById(R.id.btn_iniciar_sesion)
        val btnRegistrarse: Button = findViewById(R.id.btn_registrarse)
        val tvRegistrase: TextView = findViewById(R.id.tv_registrase)
        correoEditText = findViewById(R.id.correo)
        contraseñaEditText = findViewById(R.id.contraseña)
        recuerdameCheckBox = findViewById(R.id.Recuerdame)

        btnIniciarSesion.setOnClickListener {
            signInWithEmailAndPassword()
        }

        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, RegistryActivity::class.java)
            startActivity(intent)
        }

        tvRegistrase.setOnClickListener {
            val intent = Intent(this, RegistryActivity::class.java)
            startActivity(intent)
        }

        // Configuración de Recuerdame CheckBox
        recuerdameCheckBox.isChecked = sharedPreferences.getBoolean("recuerdameChecked", false)
        Log.d("MainActivity", "Recuerdame checked: ${recuerdameCheckBox.isChecked}")

        if (recuerdameCheckBox.isChecked) {
            val correoGuardado = sharedPreferences.getString("correo", "")
            val contraseñaGuardada = sharedPreferences.getString("contraseña", "")
            Log.d("MainActivity", "Correo guardado: $correoGuardado")
            Log.d("MainActivity", "Contraseña guardada: $contraseñaGuardada")
            correoEditText.setText(correoGuardado)
            contraseñaEditText.setText(contraseñaGuardada)
        }

        recuerdameCheckBox.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().apply {
                putBoolean("recuerdameChecked", isChecked)
                if (isChecked) {
                    putString("correo", correoEditText.text.toString())
                    putString("contraseña", contraseñaEditText.text.toString())
                } else {
                    remove("correo")
                    remove("contraseña")
                }
                apply()
            }
            Log.d("MainActivity", "Recuerdame updated: $isChecked")
        }
    }

    private fun signInWithEmailAndPassword() {
        val correoText = correoEditText.text.toString()
        val contraseñaText = contraseñaEditText.text.toString()

        if (correoText.isNotEmpty() && contraseñaText.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(correoText, contraseñaText)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Si el inicio de sesión es exitoso y la casilla de "Recuerdame" está marcada, guardar credenciales
                        if (recuerdameCheckBox.isChecked) {
                            sharedPreferences.edit().apply {
                                putString("correo", correoText)
                                putString("contraseña", contraseñaText)
                                putBoolean("recuerdameChecked", true)
                                apply()
                            }
                        }
                        // Inicio de sesión exitoso, redirigir a la actividad deseada
                        val intent = Intent(this, InicioActivity::class.java)
                        startActivity(intent)
                        finish() // Terminar MainActivity para que el usuario no pueda volver usando el botón de atrás
                    } else {
                        // Si el inicio de sesión falla, manejar el error
                        val errorMessage = when (task.exception) {
                            is FirebaseAuthInvalidUserException -> "Usuario no encontrado"
                            is FirebaseAuthInvalidCredentialsException -> "Datos incorrectos"
                            else -> "Error al iniciar sesión"
                        }
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}
