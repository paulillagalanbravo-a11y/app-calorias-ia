package com.example.appcaloriasia.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class ObjetivoRepository(context: Context) {
    private val objetivoDao = AppDatabase.getInstance(context).objetivoDao()

    fun obtenerObjetivo(): Flow<Objetivo?> = objetivoDao.obtenerObjetivo()

    suspend fun guardarObjetivo(objetivo: Objetivo) {
        objetivoDao.guardarObjetivo(objetivo)
    }
}
