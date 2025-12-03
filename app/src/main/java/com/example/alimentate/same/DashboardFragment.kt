package com.example.alimentate.same

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alimentate.data.Producto
import com.example.alimentate.databinding.FragmentDashboardBinding
import com.example.alimentate.ml.SimpleNN
import com.google.firebase.database.*


class DashboardFragment : Fragment() {

    private var _bind: FragmentDashboardBinding? = null
    private val bind get() = _bind!!

    private lateinit var db: DatabaseReference
    private lateinit var productoAdapter: ProductAdapter
    private val listaProductos = mutableListOf<Producto>()

    // Variables que mantienen los valores actuales de los sensores
    private var tempLive: Float = 0f
    private var humLive: Float = 0f
    private var gasLive: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragmentDashboardBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseDatabase.getInstance().reference

        leerSensoresTiempoReal()

        bind.btnGuardar.setOnClickListener {
            guardarAlimento()
        }

        bind.rvProductos.layoutManager = LinearLayoutManager(requireContext())
        productoAdapter = ProductAdapter(listaProductos)
        bind.rvProductos.adapter = productoAdapter
        cargarProductos()

    }

    /** LECTURA DE SENSORES EN TIEMPO REAL */
    private fun leerSensoresTiempoReal() {
        db.child("sensores").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {

                tempLive = snap.child("temperatura").getValue(Float::class.java) ?: 0f
                humLive = snap.child("humedad").getValue(Float::class.java) ?: 0f
                gasLive = snap.child("gas_raw").getValue(Float::class.java) ?: 0f

                bind.txtTempLive.text = "Temperatura: $tempLive °C"
                bind.txtHumLive.text = "Humedad: $humLive %"
                bind.txtGasLive.text = "Gas: $gasLive"
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    /** GUARDAR ALIMENTO + PREDICCIÓN */
    private fun guardarAlimento() {
        val nombre = bind.edtNombre.text.toString().trim()

        if (nombre.isEmpty()) {
            Toast.makeText(requireContext(), "Escribe un nombre", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener predicción de la red neuronal
        val pred = SimpleNN.predict(tempLive, humLive, gasLive)

        val newId = db.child("productos").push().key!!

        val data = mapOf(
            "nombre" to nombre,
            "temperatura" to tempLive,
            "humedad" to humLive,
            "gas" to gasLive,
            "riesgo" to pred.label,
            "timestamp" to System.currentTimeMillis()
        )

        db.child("productos").child(newId).setValue(data)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Producto registrado", Toast.LENGTH_SHORT).show()
                bind.edtNombre.setText("")
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cargarProductos() {
        db.child("productos").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaProductos.clear()

                for (item in snapshot.children) {
                    val producto = item.getValue(Producto::class.java)
                    if (producto != null) listaProductos.add(producto)
                }

                productoAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }
}
