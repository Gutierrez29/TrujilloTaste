package com.uct.trujillotaste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LarcoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_larco)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener las referencias de las ImageView
        val zona1 = findViewById<Button>(R.id.btnUno)
        val zona2 = findViewById<Button>(R.id.btnDos)
        val zona1uno = findViewById<Button>(R.id.btnUnoUno)

        // Configurar los OnClickListener para cada ImageView
        zona1uno.setOnClickListener {
            val intent1 = Intent(this, ZonaUno::class.java)
            startActivity(intent1)
        }

        // Configurar los OnClickListener para cada ImageView
        zona1.setOnClickListener {
            val intent1 = Intent(this, ZonaUno::class.java)
            startActivity(intent1)
        }

        zona2.setOnClickListener {
            val intent2 = Intent(this, ZonaDos::class.java)
            startActivity(intent2)
        }
    }
}