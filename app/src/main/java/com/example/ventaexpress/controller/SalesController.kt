package com.example.ventaexpress.controller
import com.example.ventaexpress.model.Sale
import java.util.concurrent.CopyOnWriteArrayList

class SalesController {

    // Lista en memoria para almacenar ventas
    private val sales = CopyOnWriteArrayList<Sale>()

    /**
     * Obtiene todas las ventas
     */
    fun fetchSales(callback: (List<Sale>) -> Unit) {
        callback(sales.toList())
    }

    /**
     * Agrega una nueva venta
     */
    fun addSale(sale: Sale, callback: (Boolean, String) -> Unit) {
        try {
            sales.add(sale)
            callback(true, "Venta registrada con éxito")
        } catch (e: Exception) {
            callback(false, e.message ?: "Error desconocido")
        }
    }

    /**
     * Elimina una venta por ID
     */
    fun deleteSale(saleId: String, callback: (Boolean, String) -> Unit) {
        val removed = sales.removeIf { it.id == saleId }
        if (removed) {
            callback(true, "Venta eliminada")
        } else {
            callback(false, "No se encontró la venta")
        }
    }
}