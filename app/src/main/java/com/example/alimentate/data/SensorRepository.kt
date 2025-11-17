package com.example.alimentate.data

import com.example.alimentate.AppConfig

/**
 * Punto único desde el que las pantallas piden datos.
 * Después vamos a meter FirebaseDataSource aquí cuando pasemos a LIVE.
 */
object SensorRepository {

    // lazy = se crea cuando se usa por primera vez
    val source: SensorDataSource by lazy {
        if (AppConfig.MODE == "LIVE") {
            // aquí después: return FirebaseDataSource(AppConfig.DEVICE_ID)
            SimDataSource() // placeholder para que compile igual
        } else {
            SimDataSource()
        }
    }
}
