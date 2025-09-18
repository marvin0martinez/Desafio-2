package com.example.ventaexpress.view


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ventaexpress.R
import com.example.ventaexpress.adapter.ClientesAdapter
import com.example.ventaexpress.controller.ClientController
import com.example.ventaexpress.model.Client
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClientesActivity : AppCompatActivity() {

    private val controller = ClientController()
    private lateinit var adapter: ClientesAdapter
    private lateinit var rvClients: RecyclerView
    private lateinit var fabAddClient: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)

        rvClients = findViewById(R.id.rvClients)
        fabAddClient = findViewById(R.id.fabAddClient)

        rvClients.layoutManager = LinearLayoutManager(this)
        adapter = ClientesAdapter(
            emptyList(),
            onEdit = { showEditDialog(it) },
            onDelete = { confirmDelete(it) }
        )
        rvClients.adapter = adapter

        fabAddClient.setOnClickListener { showAddDialog() }

        // cargar clientes desde Firebase
        controller.fetchClients { list ->
            runOnUiThread { adapter.updateList(list) }
        }
    }

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_client, null)
        val etNombre = dialogView.findViewById<android.widget.EditText>(R.id.etNombre)
        val etCorreo = dialogView.findViewById<android.widget.EditText>(R.id.etCorreo)
        val etTelefono = dialogView.findViewById<android.widget.EditText>(R.id.etTelefono)

        AlertDialog.Builder(this)
            .setTitle("Nuevo Cliente")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString().trim()
                val correo = etCorreo.text.toString().trim()
                val telefono = etTelefono.text.toString().trim()

                if (nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
                    Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val client = Client(nombre = nombre, correo = correo, telefono = telefono)
                controller.addClient(client) { ok, msg ->
                    Toast.makeText(this, if (ok) "Cliente agregado" else "Error: $msg", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showEditDialog(client: Client) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_client, null)
        val etNombre = dialogView.findViewById<android.widget.EditText>(R.id.etNombre)
        val etCorreo = dialogView.findViewById<android.widget.EditText>(R.id.etCorreo)
        val etTelefono = dialogView.findViewById<android.widget.EditText>(R.id.etTelefono)

        etNombre.setText(client.nombre)
        etCorreo.setText(client.correo)
        etTelefono.setText(client.telefono)

        AlertDialog.Builder(this)
            .setTitle("Editar Cliente")
            .setView(dialogView)
            .setPositiveButton("Actualizar") { _, _ ->
                val nombre = etNombre.text.toString().trim()
                val correo = etCorreo.text.toString().trim()
                val telefono = etTelefono.text.toString().trim()

                if (nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
                    Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val updated = client.copy(nombre = nombre, correo = correo, telefono = telefono)
                controller.updateClient(updated) { ok, msg ->
                    Toast.makeText(this, if (ok) "Cliente actualizado" else "Error: $msg", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmDelete(client: Client) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Eliminar ${client.nombre}?")
            .setPositiveButton("Sí") { _, _ ->
                controller.deleteClient(client.id) { ok, msg ->
                    Toast.makeText(this, if (ok) "Cliente eliminado" else "Error: $msg", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}