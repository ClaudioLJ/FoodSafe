package com.example.alimentate.same

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alimentate.databinding.FragmentAnalyticsBinding
import com.example.alimentate.ml.SimpleNN
import com.google.firebase.database.*

class AnalyticsFragment : Fragment() {

    private var _bind: FragmentAnalyticsBinding? = null
    private val bind get() = _bind!!

    private lateinit var db: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseDatabase.getInstance().getReference("productos")

        cargarDatos()
    }

    private fun cargarDatos() {

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val temps = mutableListOf<Float>()
                val hums = mutableListOf<Float>()
                val gases = mutableListOf<Float>()

                for (producto in snapshot.children) {
                    val temp = producto.child("temperatura").getValue(Float::class.java)
                    val hum = producto.child("humedad").getValue(Float::class.java)
                    val gas = producto.child("gas").getValue(Float::class.java)

                    if (temp != null && hum != null && gas != null) {
                        temps.add(temp)
                        hums.add(hum)
                        gases.add(gas)
                    }
                }

                procesar(temps, hums, gases)
            }

            override fun onCancelled(error: DatabaseError) {
                bind.txtAdvice.text = "Error leyendo datos: ${error.message}"
            }
        })
    }

    private fun procesar(
        temps: List<Float>,
        hums: List<Float>,
        gases: List<Float>
    ) {
        val total = temps.size
        var crit = 0

        for (i in temps.indices) {
            val pred = SimpleNN.predict(temps[i], hums[i], gases[i])
            if (pred.label == "Crítico") crit++
        }

        val pct = if (total > 0) crit * 100 / total else 0

        bind.txtCritPct.text = "$pct % de productos en estado crítico"
        bind.txtAvgHours.text = "Productos analizados: $total"

        bind.txtAdvice.text =
            if (pct > 30)
                "⚠ Riesgo alto. Revisa refrigeración o revisa manualmente los alimentos."
            else
                "Estado general estable. Los productos están en condiciones aceptables."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }
}
