package com.uct.trujillotaste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Restaurantes : AppCompatActivity() {

    private lateinit var dataArrayList: ArrayList<Usuario>
    private lateinit var recyclerView: RecyclerView // Definir recyclerView como variable
    private val base = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_restaurantes)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dataArrayList = arrayListOf()
        recyclerView = findViewById(R.id.reciclerView) // Asignar recyclerView desde el layout
        recyclerView.layoutManager =
            LinearLayoutManager(this) // Asignar LinearLayoutManager al RecyclerView
        val adapter = MainAdapter(this, dataArrayList)
        recyclerView.adapter = adapter

        llenarRecycler()

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun llenarRecycler() {
        base.collection("restaurantess")
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val dataArrayList = mutableListOf<Usuario>()
                querySnapshot?.let {
                    for (document in it.documents) {
                        val data = document.toObject(Usuario::class.java)
                        data?.let { usuario ->
                            dataArrayList.add(usuario)
                        }
                    }
                    recyclerView.adapter = MainAdapter(this, dataArrayList)
                }
            }
    }
}
