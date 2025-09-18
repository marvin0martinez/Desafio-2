package com.example.ventaexpress.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ventaexpress.R
import com.example.ventaexpress.model.SaleItem

class SaleProductAdapter(
    private val items: MutableList<SaleItem>,
    private val onQuantityChanged: () -> Unit
) : RecyclerView.Adapter<SaleProductAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        val tvProductPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val etQuantity: EditText = view.findViewById(R.id.etQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sale_product, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvProductName.text = item.nombre
        holder.tvProductPrice.text = "$${item.precio}"

        // Evitar que el TextWatcher se duplique
        holder.etQuantity.removeTextChangedListener(holder.etQuantity.tag as? TextWatcher)

        holder.etQuantity.setText(item.cantidad.toString())

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val qty = s.toString().toIntOrNull() ?: 0
                item.cantidad = qty
                onQuantityChanged() // recalcular total
            }
        }

        holder.etQuantity.addTextChangedListener(watcher)
        holder.etQuantity.tag = watcher
    }
}
