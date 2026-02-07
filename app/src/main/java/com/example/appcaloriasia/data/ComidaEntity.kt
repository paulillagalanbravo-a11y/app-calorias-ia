package com.example.appcaloriasia.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comidas")
data class ComidaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val codigoBarras: String,
    val nombre: String,
    val marca: String,
    val porcion: String,
    val calorias: Double,
    val proteina: Double,
    val hidratos: Double,
    val grasas: Double,
    val ingredientes: String,
    val fecha: String,
)
