package com.example.appcaloriasia.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "objetivos")
data class Objetivo(
    @PrimaryKey val id: Int = 0,
    val calorias: Int,
    val proteina: Int,
    val hidratos: Int,
    val grasas: Int,
)
