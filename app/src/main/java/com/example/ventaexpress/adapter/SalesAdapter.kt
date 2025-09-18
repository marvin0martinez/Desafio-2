package com.example.ventaexpress.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ventaexpress.R
import com.example.ventaexpress.model.Sale
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale



class SalesAdapter : RecyclerView.Adapter<SalesAdapter.ViewHolder>() {

    private var items: List<Sale> = emptyList()

    // ViewHolder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCliente: TextView = itemView.findViewById(R.id.tvCliente) // Asegúrate de que los IDs coincidan con tu layout
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sales, parent, false) // Asegúrate de que R.layout.item_sale sea correcto
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val sale = items[position]

        // Manejo seguro de nulls
        holder.tvCliente.text = sale.clienteNombre?.let { "Cliente: $it" } ?: "Cliente: Desconocido"

        // Formato de total con dos decimales
        holder.tvTotal.text = sale.total?.let {
            val formatter = DecimalFormat("$#,##0.00")
            "Total: ${formatter.format(it)}"
        } ?: "Total: $0.00"

        // Formato de fecha usando java.time (más seguro y moderno)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm")
                .withZone(ZoneId.systemDefault())
            holder.tvFecha.text = "Fecha: ${formatter.format(Instant.ofEpochMilli(sale.fecha))}"
        } else {
            // Fallback para versiones anteriores a Android Oreo
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            holder.tvFecha.text = "Fecha: ${sdf.format(sale.fecha)}"
        }
    }

    override fun getItemCount(): Int = items.size

    // Método para actualizar la lista con DiffUtil
    fun updateList(newItems: List<Sale>) {
        val diffCallback = SaleDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems.toList() // Copia defensiva
        diffResult.dispatchUpdatesTo(this)
    }

    // DiffUtil para actualizaciones eficientes
    private class SaleDiffCallback(
        private val oldList: List<Sale>,
        private val newList: List<Sale>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition] // Compara por referencia
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition] // Compara por contenido
        }
    }
}