package com.example.FinanPlus

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransaccionAdapter(
    private val lista: List<Transaccion>
) : RecyclerView.Adapter<TransaccionAdapter.ViewHolder>() {

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val tvNombre: TextView =
            view.findViewById(R.id.tvNombre)

        val tvTipo: TextView =
            view.findViewById(R.id.tvTipo)

        val tvMonto: TextView =
            view.findViewById(R.id.tvMonto)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.activity_transacciones,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val transaccion = lista[position]

        holder.tvNombre.text = transaccion.nombre

        holder.tvTipo.text = transaccion.tipo

        holder.tvMonto.text =
            "$%.2f".format(transaccion.monto)

        if (transaccion.tipo == "Ingreso") {

            holder.tvMonto.setTextColor(
                Color.parseColor("#39B54A")
            )

        } else {

            holder.tvMonto.setTextColor(
                Color.parseColor("#FF3B30")
            )
        }
    }

    override fun getItemCount(): Int {

        return lista.size
    }
}