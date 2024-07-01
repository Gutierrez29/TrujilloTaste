@file:Suppress("DEPRECATION")
package com.uct.trujillotaste

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class InicioActivity : AppCompatActivity() {

    private lateinit var iniciar: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_inicio)

        iniciar = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val trujillo = findViewById<Button>(R.id.btnTrujillo)
        val larco = findViewById<Button>(R.id.btnLarco)
        val moche = findViewById<Button>(R.id.btnMoche)
        val exit = findViewById<ImageButton>(R.id.exit)

        trujillo.setOnClickListener {
            Toast.makeText(this, "¡Próximamente!", Toast.LENGTH_SHORT).show()
        }
        larco.setOnClickListener {
            val intent = Intent(this, Restaurantes::class.java)
            startActivity(intent)
        }
        moche.setOnClickListener {
            Toast.makeText(this, "¡Próximamente!", Toast.LENGTH_SHORT).show()
        }

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        exit.setOnClickListener {
            signOut()
        }

    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        googleSignInClient.signOut().addOnCompleteListener(this) {
            revokeGoogleAccess()
        }
    }

    private fun revokeGoogleAccess() {
        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
            // Limpiar SharedPreferences
            sharedPreferences.edit().clear().apply()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}

