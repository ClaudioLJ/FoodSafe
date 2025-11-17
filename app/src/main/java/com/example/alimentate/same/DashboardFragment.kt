package com.example.alimentate.same

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alimentate.data.FoodItem
import com.example.alimentate.databinding.FragmentDashboardBinding
import com.example.alimentate.ml.SimpleNN

class DashboardFragment : Fragment() {

    private var _bind: FragmentDashboardBinding? = null
    private val bind get() = _bind!!

    private lateinit var adapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragmentDashboardBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Datos simulados duros para mostrar inventario + predicción ML
        val rawItems = listOf(
            Triple("Pechuga de pollo", Triple(6.5f, 82f, 1800), 24),
            Triple("Leche entera",     Triple(9.0f, 70f, 1200), 12),
            Triple("Fresas",           Triple(12.0f, 88f, 2500), 8),
            Triple("Carne molida",     Triple(10.5f, 90f, 3000), 6),
            Triple("Queso Oaxaca",     Triple(7.5f, 75f, 1600), 36)
        )

        val foodItems = rawItems.map { (name, sensor, hoursLeft) ->
            val (t, h, g) = sensor
            val pred = SimpleNN.predict(t, h, g)
            FoodItem(
                name = name,
                tempC = t,
                humi = h,
                gas = g,
                freshnessClass = pred.label,
                hoursLeft = hoursLeft
            )
        }

        adapter = FoodAdapter(foodItems)

        bind.recyclerFoods.layoutManager = LinearLayoutManager(requireContext())
        bind.recyclerFoods.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }
}
