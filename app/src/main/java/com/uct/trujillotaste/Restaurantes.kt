package com.uct.trujillotaste

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Restaurantes : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView // Definir recyclerView como variable
    private lateinit var adapter: MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_restaurantes)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        adapter = MainAdapter(this)
        recyclerView = findViewById(R.id.reciclerView) // Asignar recyclerView desde el layout
        recyclerView.layoutManager =
            LinearLayoutManager(this) // Asignar LinearLayoutManager al RecyclerView
        recyclerView.adapter = adapter

        val dummylist = mutableListOf<Usuario>()
        dummylist.add(
            Usuario(
                "https://restaurantebigbentrujillo.com/wp-content/uploads/2021/02/almuerzos-en-trujillo-1030x953.jpg",
                "GOKU",
                "Mejor restaurante de perukistan",
                "Fino",
                4.5))


        adapter.setListData(dummylist)
        adapter.notifyDataSetChanged()
    }
}
