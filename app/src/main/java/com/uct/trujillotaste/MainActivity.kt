package com.uct.trujillotaste

import android.content.Intent
import android.content.SharedPreferences
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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "prefs"
    private val PREF_EMAIL = "email"
    private val PREF_PASSWORD = "password"
    private val PREF_REMEMBER = "remember"

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

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Configurar los listeners para los botones
        val btnRegistrarse: Button = findViewById(R.id.btn_registrarse)
        val tvRegistrase: TextView = findViewById(R.id.tv_registrase)
        val btnIniciarSesion: Button = findViewById(R.id.btn_iniciar_sesion)
        val correo: EditText = findViewById(R.id.correo)
        val contraseña: EditText = findViewById(R.id.contraseña)
        val checkBox: CheckBox = findViewById(R.id.Recuerdame)

        loadPreferences(correo, contraseña, checkBox)

        // Si el usuario está recordado, intentar iniciar sesión automáticamente
        if (checkBox.isChecked) {
            signInAutomatically(correo.text.toString(), contraseña.text.toString())
        }

        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, RegistryActivity::class.java)
            startActivity(intent)
        }

        tvRegistrase.setOnClickListener {
            val intent = Intent(this, RegistryActivity::class.java)
            startActivity(intent)
        }

        setup(btnIniciarSesion, correo, contraseña, checkBox)
    }

    private fun setup(btnIniciarSesion: Button, correo: EditText, contraseña: EditText, checkBox: CheckBox) {
        title = "Autenticacion"
        btnIniciarSesion.setOnClickListener {
            val correoText = correo.text.toString()
            val contraseñaText = contraseña.text.toString()

            if (correoText.isNotEmpty() && contraseñaText.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(correoText, contraseñaText)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Guardar preferencias si el checkbox está marcado
                            if (checkBox.isChecked) {
                                savePreferences(correoText, contraseñaText, true)
                            } else {
                                clearPreferences()
                            }

                            // Inicio de sesión exitoso, redirigir a la actividad deseada
                            val intent = Intent(this, inicioActivity::class.java)
                            startActivity(intent)
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

    private fun savePreferences(email: String, password: String, remember: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putString(PREF_EMAIL, email)
        editor.putString(PREF_PASSWORD, password)
        editor.putBoolean(PREF_REMEMBER, remember)
        editor.apply()
    }

    private fun clearPreferences() {
        val editor = sharedPreferences.edit()
        editor.remove(PREF_EMAIL)
        editor.remove(PREF_PASSWORD)
        editor.remove(PREF_REMEMBER)
        editor.apply()
    }

    private fun loadPreferences(correo: EditText, contraseña: EditText, checkBox: CheckBox) {
        val email = sharedPreferences.getString(PREF_EMAIL, "")
        val password = sharedPreferences.getString(PREF_PASSWORD, "")
        val remember = sharedPreferences.getBoolean(PREF_REMEMBER, false)

        correo.setText(email)
        contraseña.setText(password)
        checkBox.isChecked = remember
    }

    private fun signInAutomatically(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso, redirigir a la actividad deseada
                    val intent = Intent(this, inicioActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Si el inicio de sesión falla, manejar el error
                    clearPreferences() // Limpiar las preferencias si el inicio de sesión automático falla
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "Usuario no encontrado"
                        is FirebaseAuthInvalidCredentialsException -> "Datos incorrectos"
                        else -> "Error al iniciar sesión"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
