package com.example.ventaexpress.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ventaexpress.R
import com.example.ventaexpress.adapter.SalesAdapter
import com.example.ventaexpress.adapter.SaleProductAdapter
import com.example.ventaexpress.controller.ClientController
import com.example.ventaexpress.controller.ProductController
import com.example.ventaexpress.controller.SalesController
import com.example.ventaexpress.model.Product
import com.example.ventaexpress.model.Sale
import com.example.ventaexpress.model.SaleItem
import com.example.ventaexpress.model.Client
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SalesActivity : AppCompatActivity() {

    private val saleController = SalesController()
    private val productController = ProductController()
    private val clientController = ClientController()

    private lateinit var adapter: SalesAdapter
    private lateinit var rvSales: RecyclerView
    private lateinit var fabAddSale: FloatingActionButton

    private var productList: List<Product> = emptyList()
    private var clientList: List<Client> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales)

        rvSales = findViewById(R.id.rvSales)
        fabAddSale = findViewById(R.id.fabAddSale)

        rvSales.layoutManager = LinearLayoutManager(this)
        adapter = SalesAdapter()
        rvSales.adapter = adapter

        fabAddSale.setOnClickListener { showAddSaleDialog() }

        // cargar ventas iniciales con tipo explícito
        saleController.fetchSales { list: List<Sale> ->
            runOnUiThread { adapter.updateList(list) }
        }

        // precargar clientes y productos con tipo explícito
        clientController.fetchClients { clients: List<Client> ->
            clientList = clients
        }

        productController.fetchProducts { products: List<Product> ->
            productList = products
        }
    }

    private fun showAddSaleDialog() {
        if (clientList.isEmpty() || productList.isEmpty()) {
            Toast.makeText(this, "Debe haber al menos 1 cliente y 1 producto", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_sale, null)
        val spClientes = dialogView.findViewById<android.widget.Spinner>(R.id.spClientes)
        val rvSaleProducts = dialogView.findViewById<RecyclerView>(R.id.rvSaleProducts)
        val tvTotal = dialogView.findViewById<android.widget.TextView>(R.id.tvTotal)

        // spinner clientes
        val clientNames = clientList.map { it.nombre }
        spClientes.adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, clientNames)

        // adapter productos con cantidades
        val saleItems = productList.map { SaleItem(it.id, it.nombre, it.precio, 0) }.toMutableList()
        val productAdapter = SaleProductAdapter(saleItems) {
            val total: Double = saleItems.sumOf { it.precio * it.cantidad }
            tvTotal.text = "Total: $${total}"
        }
        rvSaleProducts.layoutManager = LinearLayoutManager(this)
        rvSaleProducts.adapter = productAdapter

        AlertDialog.Builder(this)
            .setTitle("Nueva Venta")
            .setView(dialogView)
            .setPositiveButton("Registrar") { _, _ ->
                val cliente: Client = clientList[spClientes.selectedItemPosition]
                val selectedItems: List<SaleItem> = saleItems.filter { it.cantidad > 0 }

                if (selectedItems.isEmpty()) {
                    Toast.makeText(this, "Debe seleccionar al menos un producto", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                for (item in selectedItems) {
                    val prod: Product? = productList.find { it.id == item.productId }
                    if (prod != null && item.cantidad > prod.stock) {
                        Toast.makeText(this, "Stock insuficiente para ${item.nombre}", Toast.LENGTH_LONG).show()
                        return@setPositiveButton
                    }
                }

                val total: Double = selectedItems.sumOf { it.precio * it.cantidad }
                val sale = Sale(
                    clienteId = cliente.id,
                    clienteNombre = cliente.nombre,
                    listaProductos = selectedItems,
                    total = total
                )

                saleController.addSale(sale) { ok: Boolean, msg: String? ->
                    Toast.makeText(this, if (ok) "Venta registrada" else "Error: $msg", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
