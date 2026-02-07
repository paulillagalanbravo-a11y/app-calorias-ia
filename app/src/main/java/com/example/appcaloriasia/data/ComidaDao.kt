package com.example.appcaloriasia.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ComidaDao {
    @Insert
    suspend fun insertarComida(comida: ComidaEntity)

    @Query("SELECT * FROM comidas ORDER BY fecha DESC, id DESC")
    fun obtenerHistorial(): Flow<List<ComidaEntity>>

    @Query("SELECT * FROM comidas WHERE fecha = :fecha")
    fun obtenerComidasPorFecha(fecha: String): Flow<List<ComidaEntity>>
}
