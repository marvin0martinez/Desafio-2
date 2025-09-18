package com.example.ventaexpress.model


data class SaleItem(
    val productId: String,
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)
