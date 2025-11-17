package com.example.alimentate.data

import kotlinx.coroutines.flow.Flow

/**
 * Esta interfaz describe "de dónde vienen los datos".
 * Puede ser SIM (datos falsos) o LIVE (Firebase real).
 */
interface SensorDataSource {

    // Última lectura en "tiempo real"
    fun lastReadingFlow(): Flow<SensorReading?>

    // Histórico (lista de lecturas pasadas)
    fun historyFlow(limit: Int = 50): Flow<List<SensorReading>>
}
