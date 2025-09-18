package com.example.ventaexpress.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ventaexpress.R
import com.example.ventaexpress.model.Client
class ClientesAdapter(
    private var clientes: List<Client>,
    private val onEdit: (Client) -> Unit,
    private val onDelete: (Client) -> Unit
) : RecyclerView.Adapter<ClientesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvCorreo: TextView = view.findViewById(R.id.tvCorreo)
        val tvTelefono: TextView = view.findViewById(R.id.tvTelefono)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clientes, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = clientes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val client = clientes[position]
        holder.tvNombre.text = client.nombre
        holder.tvCorreo.text = client.correo
        holder.tvTelefono.text = client.telefono

        holder.btnEdit.setOnClickListener { onEdit(client) }
        holder.btnDelete.setOnClickListener { onDelete(client) }
    }

    fun updateList(newList: List<Client>) { // 👈 aquí estaba el error
        clientes = newList
        notifyDataSetChanged()
    }
}