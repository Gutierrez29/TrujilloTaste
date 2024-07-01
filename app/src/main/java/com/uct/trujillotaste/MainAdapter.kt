package com.uct.trujillotaste

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class MainAdapter(
    private val context: Context,
    private var datalist: MutableList<Usuario>) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {



    fun setListData(data: MutableList<Usuario>) {
        datalist = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val user: Usuario = datalist[position]
        holder.bindView(user)
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val circleImageView: CircleImageView = itemView.findViewById(R.id.circleImageView)
        private val txtTitle: TextView = itemView.findViewById(R.id.txt_title)
        private val txtDesc: TextView = itemView.findViewById(R.id.txt_desc)
        private val txtCategory: TextView = itemView.findViewById(R.id.txt_category)
        private val txtRating: TextView = itemView.findViewById(R.id.txt_rating)

        fun bindView(user: Usuario) {
            Glide.with(context).load(user.imageUrl).into(circleImageView)
            txtTitle.text = user.nombre
            txtDesc.text = user.descripcion
            txtCategory.text = user.categoria
            txtRating.text = user.rating.toString()
            // Aquí puedes asignar una imagen a ratingStar si es necesario
        }
    }
}
