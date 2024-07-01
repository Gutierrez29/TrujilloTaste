@file:Suppress("DEPRECATION", "PrivatePropertyName")

package com.uct.trujillotaste

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    private lateinit var iniciar: FirebaseAuth
    private val RC_SIGN_IN = 9001
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
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

        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
        iniciar = FirebaseAuth.getInstance()

        // Configuración de Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

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

        val iniciarGoogle = findViewById<ImageButton>(R.id.google)
        iniciarGoogle.setOnClickListener {
            signInWithGoogle()
        }

    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(Exception::class.java)
            if (account != null) {
                iniciarGoogle(account.idToken!!)
            }
        } catch (_: Exception) {
        }
    }

    private fun iniciarGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        iniciar.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = iniciar.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
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
                        val user = iniciar.currentUser
                        updateUI(user)
                        // Inicio de sesión exitoso, redirigir a la actividad deseada
                        val intent = Intent(this, InicioActivity::class.java)
                        startActivity(intent)
                        finish() // Terminar MainActivity para que el usuario no pueda volver usando el botón de atrás
                    } else {
                        updateUI(null)
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

    override fun onStart() {
        super.onStart()
        val currentUser = iniciar.currentUser
        if (currentUser != null) {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, InicioActivity::class.java))
            finish()
        } else {
            googleSignInClient.signOut().addOnCompleteListener {
                googleSignInClient.revokeAccess().addOnCompleteListener {
                    val correo = findViewById<EditText>(R.id.correo)
                    val contraseña = findViewById<EditText>(R.id.contraseña)
                    correo.text.clear()
                    contraseña.text.clear()
                    correo.requestFocus()
                }
            }
        }
    }

}
