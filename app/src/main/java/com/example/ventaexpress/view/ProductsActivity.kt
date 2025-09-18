package com.example.ventaexpress.view

import com.example.ventaexpress.adapter.ProductAdapter
import com.example.ventaexpress.model.Product
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ventaexpress.R
import com.example.ventaexpress.controller.ProductController
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ProductsActivity : AppCompatActivity() {

    private val controller = ProductController()
    private lateinit var adapter: ProductAdapter
    private lateinit var rvProducts: RecyclerView
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        rvProducts = findViewById(R.id.rvProducts)
        fabAdd = findViewById(R.id.fabAddProduct)

        LinearLayoutManager(this).also { rvProducts.layoutManager = it }
        adapter = ProductAdapter(
            emptyList(),
            onEdit = { showEditDialog(it) },
            onDelete = { confirmDelete(it) }
        )
        rvProducts.adapter = adapter

        fabAdd.setOnClickListener { showAddDialog() }

        // cargar datos desde Firebase
        controller.fetchProducts { list ->
            runOnUiThread { adapter.updateList(list) }
        }
    }

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_product, null)
        val etNombre = dialogView.findViewById<android.widget.EditText>(R.id.etNombre)
        val etDescripcion = dialogView.findViewById<android.widget.EditText>(R.id.etDescripcion)
        val etPrecio = dialogView.findViewById<android.widget.EditText>(R.id.etPrecio)
        val etStock = dialogView.findViewById<android.widget.EditText>(R.id.etStock)

        AlertDialog.Builder(this)
            .setTitle("Nuevo Producto")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString().trim()
                val desc = etDescripcion.text.toString().trim()
                val precio = etPrecio.text.toString().toDoubleOrNull()
                val stock = etStock.text.toString().toIntOrNull()

                if (nombre.isEmpty() || precio == null || stock == null) {
                    Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val prod = Product(nombre = nombre, descripcion = desc, precio = precio, stock = stock)
                controller.addProduct(prod) { ok, msg ->
                    Toast.makeText(this, if (ok) "Producto agregado" else "Error: $msg", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showEditDialog(product: Product) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_product, null)
        val etNombre = dialogView.findViewById<android.widget.EditText>(R.id.etNombre)
        val etDescripcion = dialogView.findViewById<android.widget.EditText>(R.id.etDescripcion)
        val etPrecio = dialogView.findViewById<android.widget.EditText>(R.id.etPrecio)
        val etStock = dialogView.findViewById<android.widget.EditText>(R.id.etStock)

        etNombre.setText(product.nombre)
        etDescripcion.setText(product.descripcion)
        etPrecio.setText(product.precio.toString())
        etStock.setText(product.stock.toString())

        AlertDialog.Builder(this)
            .setTitle("Editar Producto")
            .setView(dialogView)
            .setPositiveButton("Actualizar") { _, _ ->
                val nombre = etNombre.text.toString().trim()
                val desc = etDescripcion.text.toString().trim()
                val precio = etPrecio.text.toString().toDoubleOrNull()
                val stock = etStock.text.toString().toIntOrNull()

                if (nombre.isEmpty() || precio == null || stock == null) {
                    Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val updated = product.copy(nombre = nombre, descripcion = desc, precio = precio, stock = stock)
                controller.updateProduct(updated) { ok, msg ->
                    Toast.makeText(this, if (ok) "Producto actualizado" else "Error: $msg", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmDelete(product: Product) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Eliminar ${product.nombre}?")
            .setPositiveButton("Sí") { _, _ ->
                controller.deleteProduct(product.id) { ok, msg ->
                    Toast.makeText(this, if (ok) "Producto eliminado" else "Error: $msg", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}