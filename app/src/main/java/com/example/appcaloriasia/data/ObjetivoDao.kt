package com.example.appcaloriasia.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjetivoDao {
    @Query("SELECT * FROM objetivos WHERE id = 0")
    fun obtenerObjetivo(): Flow<Objetivo?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarObjetivo(objetivo: Objetivo)
}
