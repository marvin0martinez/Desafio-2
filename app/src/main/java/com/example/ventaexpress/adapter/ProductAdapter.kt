package com.example.ventaexpress.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ventaexpress.R
import com.example.ventaexpress.model.Product

class ProductAdapter(
    private var items: List<Product>,
    private val onEdit: (Product) -> Unit,
    private val onDelete: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val tvPrecio: TextView = view.findViewById(R.id.tvPrecio)
        val tvStock: TextView = view.findViewById(R.id.tvStock)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = items[position]
        holder.tvNombre.text = product.nombre
        holder.tvDescripcion.text = product.descripcion
        holder.tvPrecio.text = "Precio: $${product.precio}"
        holder.tvStock.text = "Stock: ${product.stock}"

        holder.btnEdit.setOnClickListener { onEdit(product) }
        holder.btnDelete.setOnClickListener { onDelete(product) }
    }


    fun updateList(newItems: List<Product>) {
        items = newItems
        notifyDataSetChanged()
    }
}