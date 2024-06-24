package com.uct.trujillotaste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegistryActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        auth = FirebaseAuth.getInstance()

        val correoEditText: EditText = findViewById(R.id.crear_Correo)
        val contraseñaEditText: EditText = findViewById(R.id.contraseña)
        val btnRegistrarse: Button = findViewById(R.id.btn_registrase2)
        val volverTextView: TextView = findViewById(R.id.volver_pll_inicio)

        btnRegistrarse.setOnClickListener {
            val correo = correoEditText.text.toString()
            val contraseña = contraseñaEditText.text.toString()

            // Registrar el usuario en Firebase Authentication
            auth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registro exitoso, redirigir a MainActivity
                        Toast.makeText(this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        // Si el registro falla, mostrar un mensaje al usuario
                        Toast.makeText(this, "Error al crear la cuenta", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        volverTextView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
