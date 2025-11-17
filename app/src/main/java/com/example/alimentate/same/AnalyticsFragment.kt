package com.example.alimentate.same

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alimentate.databinding.FragmentAnalyticsBinding
import com.example.alimentate.ml.SimpleNN

class AnalyticsFragment : Fragment() {

    private var _bind: FragmentAnalyticsBinding? = null
    private val bind get() = _bind!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val temps   = listOf(6.5f, 12f, 10.5f, 9.0f)
        val hums    = listOf(82f, 88f, 90f, 70f)
        val gases   = listOf(1800, 2500, 3000, 1200)
        val hoursLeft = listOf(24, 8, 6, 12)

        var criticalCount = 0
        var total = temps.size
        var sumHours = 0

        for (i in temps.indices) {
            val pred = SimpleNN.predict(temps[i], hums[i], gases[i])
            if (pred.label == "Crítico") criticalCount++
            sumHours += hoursLeft[i]
        }

        val critPct = if (total > 0) (criticalCount * 100 / total) else 0
        val avgHours = if (total > 0) sumHours / total else 0

        bind.txtCritPct.text = "$critPct % de productos en estado crítico"
        bind.txtAvgHours.text = "Vida útil promedio: ~${avgHours} h"
        bind.txtAdvice.text =
            if (critPct > 30)
                "ALERTA: Alto riesgo de desperdicio. Recomendado consumir / revisar temperatura."
            else
                "Consumo bajo control. Mantener refrigeración estable."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }
}
