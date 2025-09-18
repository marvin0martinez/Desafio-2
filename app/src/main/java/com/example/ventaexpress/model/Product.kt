package com.example.ventaexpress.model

data class Product(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String? = null,
    val precio: Double = 0.0,
    val stock: Int = 0
)