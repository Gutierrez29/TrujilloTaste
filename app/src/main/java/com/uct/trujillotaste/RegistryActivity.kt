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
        val volverInicioTextView: TextView = findViewById(R.id.volver_pll_inicio)

        btnRegistrarse.setOnClickListener {
            val correo = correoEditText.text.toString()
            val contraseña = contraseñaEditText.text.toString()

            // Registrar el usuario en Firebase Authentication
            auth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registro exitoso, mostrar mensaje y redirigir a MainActivity
                        Toast.makeText(this, "Cuenta registrada correctamente", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        // Si el registro falla, mostrar mensaje de error
                        Toast.makeText(this, "Error al registrar cuenta: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        volverInicioTextView.setOnClickListener {
            // Volver al MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
