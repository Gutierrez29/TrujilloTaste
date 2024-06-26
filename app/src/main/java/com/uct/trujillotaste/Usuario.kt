package com.uct.trujillotaste

data class Usuario(
    val imageUrl: String = "DEFAULT URL",
    val nombre: String = "DEFAULT NAME",
    val descripcion: String = "DEFAULT DESCRIPCION",
    val categoria: String = "DEFAULT CATEGORIA",
    val rating: Double = 5.0)
