package com.example.alimentate.same
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.alimentate.R
import com.example.alimentate.data.Producto
import androidx.core.graphics.toColorInt

class ProductAdapter(
    private val productList: List<Producto>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre = itemView.findViewById<TextView>(R.id.txtCardNombre)
        val estado = itemView.findViewById<TextView>(R.id.txtCardRiesgo)
        val humedad = itemView.findViewById<TextView>(R.id.txtCardHum)
        val temperatura = itemView.findViewById<TextView>(R.id.txtCardTemp)
        val gas = itemView.findViewById<TextView>(R.id.txtCardGas)
        val card = itemView as CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductViewHolder(view)
    }


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val producto = productList[position]

        holder.nombre.text = producto.nombre
        holder.estado.text = producto.riesgo
        holder.humedad.text = "Humedad: ${producto.humedad}"
        holder.temperatura.text = "Temperatura: ${producto.temperatura}"
        holder.gas.text = "Gas: ${producto.gas}"


        when (producto.riesgo) {
            "Bueno" -> holder.card.setCardBackgroundColor("#C8E6C9".toColorInt())   // Verde suave
            "Riesgo" -> holder.card.setCardBackgroundColor("#FFF9C4".toColorInt()) // Amarillo suave
            "Vencido" -> holder.card.setCardBackgroundColor("#FFCDD2".toColorInt())  // Rojo suave
        }
    }

    override fun getItemCount() = productList.size
}
