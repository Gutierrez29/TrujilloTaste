package com.uct.trujillotaste

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView


class InicioActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val trujillo = findViewById<Button>(R.id.btnTrujillo)
        val larco = findViewById<Button>(R.id.btnLarco)
        val moche = findViewById<Button>(R.id.btnMoche)

        trujillo.setOnClickListener {
            val intent = Intent(this, TrujilloActivity::class.java)
            startActivity(intent)
        }
        larco.setOnClickListener {
            val intent = Intent(this, LarcoActivity::class.java)
            startActivity(intent)
        }
        moche.setOnClickListener {
            val intent = Intent(this, MocheActivity::class.java)
            startActivity(intent)
        }

    }

}

