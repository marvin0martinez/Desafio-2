package com.example.ventaexpress.controller

import com.example.ventaexpress.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class ProductController {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("productos")

    // Obtener todos los productos
    fun fetchProducts(callback: (List<Product>) -> Unit) {
        collection.get()
            .addOnSuccessListener { result ->
                val products = result.map { doc ->
                    Product(
                        id = doc.id,
                        nombre = doc.getString("nombre") ?: "",
                        descripcion = doc.getString("descripcion") ?: "",
                        precio = doc.getDouble("precio") ?: 0.0,
                        stock = (doc.getLong("stock") ?: 0).toInt()
                    )
                }
                callback(products)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    // Agregar producto
    fun addProduct(product: Product, callback: (Boolean, String) -> Unit) {
        val data = hashMapOf(
            "nombre" to product.nombre,
            "descripcion" to product.descripcion,
            "precio" to product.precio,
            "stock" to product.stock
        )
        collection.add(data)
            .addOnSuccessListener { callback(true, "Producto agregado") }
            .addOnFailureListener { e -> callback(false, e.message ?: "Error desconocido") }
    }

    // Actualizar producto
    fun updateProduct(product: Product, callback: (Boolean, String) -> Unit) {
        val data = hashMapOf(
            "nombre" to product.nombre,
            "descripcion" to product.descripcion,
            "precio" to product.precio,
            "stock" to product.stock
        )
        collection.document(product.id)
            .set(data)
            .addOnSuccessListener { callback(true, "Producto actualizado") }
            .addOnFailureListener { e -> callback(false, e.message ?: "Error desconocido") }
    }

    // Eliminar producto
    fun deleteProduct(id: String, callback: (Boolean, String) -> Unit) {
        collection.document(id)
            .delete()
            .addOnSuccessListener { callback(true, "Producto eliminado") }
            .addOnFailureListener { e -> callback(false, e.message ?: "Error desconocido") }
    }
}