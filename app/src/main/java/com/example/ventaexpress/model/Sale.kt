package com.example.ventaexpress.model

import java.util.Date
import java.util.UUID

data class Sale(
    val id: String = UUID.randomUUID().toString(), // ID único automático
    val clienteId: String,
    val clienteNombre: String,
    val listaProductos: List<SaleItem>,
    val total: Double,
    val fecha: Long = Date().time
)
