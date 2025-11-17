package com.example.alimentate.data

data class FoodItem(
    val name: String,
    val tempC: Float,
    val humi: Float,
    val gas: Int,
    val freshnessClass: String, // "Fresco", "Riesgo", "Crítico"
    val hoursLeft: Int          // vida útil estimada aprox.
)
