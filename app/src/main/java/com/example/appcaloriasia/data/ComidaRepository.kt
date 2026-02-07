package com.example.appcaloriasia.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class ComidaRepository(context: Context) {
    private val comidaDao = AppDatabase.getInstance(context).comidaDao()

    suspend fun guardarComida(comida: ComidaEntity) {
        comidaDao.insertarComida(comida)
    }

    fun obtenerHistorial(): Flow<List<ComidaEntity>> = comidaDao.obtenerHistorial()

    fun obtenerComidasPorFecha(fecha: String): Flow<List<ComidaEntity>> =
        comidaDao.obtenerComidasPorFecha(fecha)
}
