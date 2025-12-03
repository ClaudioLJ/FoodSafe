package com.example.alimentate.data


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseDataSource {

    // referencia al nodo "sensores" en la raíz del Realtime Database
    private val dbRef: DatabaseReference =
        FirebaseDatabase.getInstance()
            .getReference("sensores")

    // Te doy la lectura como Flow<SensorReading> para poder recolectarla fácil en el Fragment
    fun liveReadings(): Flow<SensorReading> = callbackFlow {
        val listener = object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {

                // Leemos cada hijo del nodo "sensores"
                val tempValue = snapshot.child("temperatura").getValue(Float::class.java)
                val humValue  = snapshot.child("humedad").getValue(Float::class.java)
                val gasValue  = snapshot.child("gas_raw").getValue(Float::class.java)

                val reading = SensorReading(
                    tempC = tempValue ?: 0f,
                    humiPct  = humValue  ?: 0f,
                    gasRaw   = gasValue  ?: 0f,
                    tsUnix = System.currentTimeMillis(),
                )

                trySend(reading)
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                // si Firebase falla (permiso, desconexión, etc) podemos ignorar o loguear.
                // Para demo no hacemos throw, sólo no mandamos nada.
            }
        }

        dbRef.addValueEventListener(listener)

        awaitClose {
            dbRef.removeEventListener(listener)
        }
    }
}

