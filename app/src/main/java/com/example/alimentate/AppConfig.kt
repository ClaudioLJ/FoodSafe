package com.example.alimentate

object AppConfig {
    // SIM = usamos datos simulados internos (sin hardware)
    // LIVE = vamos a jalar de Firebase cuando el ESP32 ya suba datos reales
    const val MODE = "SIM"

    // Este ID debe ser el mismo que va a usar el ESP32 cuando mande a Firebase
    const val DEVICE_ID = "ABCDEF123456"
}
