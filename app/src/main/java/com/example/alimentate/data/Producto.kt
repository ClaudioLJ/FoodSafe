package com.example.alimentate.data

data class Producto(
    val nombre: String = "",
    val temperatura: Float = 0f,
    val humedad: Float = 0f,
    val gas: Float = 0f,
    val riesgo: String = "",
    val confianza: Float = 0f
)

