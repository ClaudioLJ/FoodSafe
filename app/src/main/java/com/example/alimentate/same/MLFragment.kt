package com.example.alimentate.same

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.alimentate.data.FirebaseDataSource
import com.example.alimentate.data.SensorReading
import com.example.alimentate.databinding.FragmentMlBinding
import com.example.alimentate.ml.SimpleNN
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MLFragment : Fragment() {

    private var _bind: FragmentMlBinding? = null
    private val bind get() = _bind!!

    private val firebaseDS = FirebaseDataSource()

    private var liveJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragmentMlBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Nos suscribimos al flujo en vivo de Firebase
        liveJob = viewLifecycleOwner.lifecycleScope.launch {
            firebaseDS.liveReadings().collectLatest { reading ->
                updateUIFromReading(reading)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        liveJob?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }

    /**
     * Actualiza el texto en pantalla usando:
     * - Lectura real del ESP32 (Firebase)
     * - Clasificación ML (SimpleNN)
     */
    private fun updateUIFromReading(r: SensorReading) {
        // Ejecutamos la "red neuronal"
        val pred = SimpleNN.predict(r.tempC, r.humi, r.gas)

        // Actualizamos la UI
        bind.txtTempLive.text = String.format("Temp: %.1f °C", r.tempC)
        bind.txtHumiLive.text = String.format("Humedad: %.1f %%", r.humi)
        bind.txtGasLive.text  = "Gas: ${r.gas}"

        bind.txtClassLive.text = "Clasificación: ${pred.label}"

        bind.txtProbLive.text = String.format(
            "P(Fresco)=%.2f  P(Riesgo)=%.2f  P(Crítico)=%.2f",
            pred.pFresco, pred.pRiesgo, pred.pCritico
        )
    }
}
