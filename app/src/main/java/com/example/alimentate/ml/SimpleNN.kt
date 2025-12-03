package com.example.alimentate.ml

import kotlin.math.exp

object SimpleNN {

    private val W1 = arrayOf(
        floatArrayOf( 0.08f,  0.03f,  0.005f),
        floatArrayOf( 0.02f,  0.07f,  0.010f),
        floatArrayOf(-0.06f,  0.04f,  0.020f),
        floatArrayOf( 0.10f, -0.05f,  0.015f)
    )
    private val b1 = floatArrayOf(0.1f, 0.05f, -0.08f, 0.02f)

    private val W2 = arrayOf(
        floatArrayOf( 0.6f, -0.2f, -0.1f,  0.3f),  // Fresco
        floatArrayOf(-0.1f,  0.5f,  0.2f, -0.2f),  // Riesgo
        floatArrayOf(-0.3f,  0.1f,  0.6f,  0.1f)   // Crítico
    )
    private val b2 = floatArrayOf(0.0f, 0.1f, -0.05f)

    data class Prediction(
        val label: String,
        val pFresco: Float,
        val pRiesgo: Float,
        val pCritico: Float
    )

    private fun relu(x: Float) = if (x > 0f) x else 0f

    private fun softmax(logits: FloatArray): FloatArray {
        val maxLogit = logits.maxOrNull() ?: 0f
        val exps = FloatArray(logits.size) { i ->
            exp((logits[i] - maxLogit).toDouble()).toFloat()
        }
        val sum = exps.sum()
        return FloatArray(logits.size) { i -> exps[i] / sum }
    }

    fun predict(tempC: Float, humi: Float, gas: Float): Prediction {
        val t = tempC / 50f      // temp esperada 0-50°C
        val h = humi / 100f      // humedad 0-100%
        val g = gas / 4095f      // lectura ADC ESP32 ~0-4095

        val hidden = FloatArray(4) { 0f }
        for (n in 0 until 4) {
            hidden[n] =
                W1[n][0] * t +
                        W1[n][1] * h +
                        W1[n][2] * g +
                        b1[n]
            hidden[n] = relu(hidden[n])
        }

        val logits = FloatArray(3) { 0f }
        for (o in 0 until 3) {
            logits[o] =
                W2[o][0] * hidden[0] +
                        W2[o][1] * hidden[1] +
                        W2[o][2] * hidden[2] +
                        W2[o][3] * hidden[3] +
                        b2[o]
        }

        val probs = softmax(logits)
        val labels = arrayOf("Fresco", "Riesgo", "Crítico")

        var bestIdx = 0
        var bestVal = probs[0]
        for (i in 1 until probs.size) {
            if (probs[i] > bestVal) {
                bestVal = probs[i]
                bestIdx = i
            }
        }

        return Prediction(
            label = labels[bestIdx],
            pFresco = probs[0],
            pRiesgo = probs[1],
            pCritico = probs[2]
        )
    }
}
