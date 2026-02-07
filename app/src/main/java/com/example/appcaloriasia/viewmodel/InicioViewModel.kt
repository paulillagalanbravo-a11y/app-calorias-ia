package com.example.appcaloriasia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appcaloriasia.data.ComidaRepository
import com.example.appcaloriasia.data.ObjetivoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class InicioViewModel(
    comidaRepository: ComidaRepository,
    objetivoRepository: ObjetivoRepository,
) : ViewModel() {
    val titulo: String = "Inicio"

    private val objetivosPorDefecto = ObjetivosDiarios(
        calorias = 2000,
        proteina = 120,
        hidratos = 220,
        grasas = 70,
    )

    private val fechaHoy = LocalDate.now().toString()

    val resumen: StateFlow<ResumenDiario> = combine(
        comidaRepository.obtenerComidasPorFecha(fechaHoy),
        objetivoRepository.obtenerObjetivo(),
    ) { comidas, objetivo ->
        val objetivos = objetivo?.let {
            ObjetivosDiarios(
                calorias = it.calorias,
                proteina = it.proteina,
                hidratos = it.hidratos,
                grasas = it.grasas,
            )
        } ?: objetivosPorDefecto

        val caloriasConsumidas = comidas.sumOf { it.calorias }
        val proteinaConsumida = comidas.sumOf { it.proteina }
        val hidratosConsumidos = comidas.sumOf { it.hidratos }
        val grasasConsumidas = comidas.sumOf { it.grasas }

        ResumenDiario(
            caloriasConsumidas = caloriasConsumidas,
            caloriasObjetivo = objetivos.calorias,
            proteinaConsumida = proteinaConsumida,
            proteinaObjetivo = objetivos.proteina,
            hidratosConsumidos = hidratosConsumidos,
            hidratosObjetivo = objetivos.hidratos,
            grasasConsumidas = grasasConsumidas,
            grasasObjetivo = objetivos.grasas,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        ResumenDiario(
            caloriasConsumidas = 0.0,
            caloriasObjetivo = objetivosPorDefecto.calorias,
            proteinaConsumida = 0.0,
            proteinaObjetivo = objetivosPorDefecto.proteina,
            hidratosConsumidos = 0.0,
            hidratosObjetivo = objetivosPorDefecto.hidratos,
            grasasConsumidas = 0.0,
            grasasObjetivo = objetivosPorDefecto.grasas,
        ),
    )
}

class InicioViewModelFactory(
    private val comidaRepository: ComidaRepository,
    private val objetivoRepository: ObjetivoRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InicioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InicioViewModel(comidaRepository, objetivoRepository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}

data class ResumenDiario(
    val caloriasConsumidas: Double,
    val caloriasObjetivo: Int,
    val proteinaConsumida: Double,
    val proteinaObjetivo: Int,
    val hidratosConsumidos: Double,
    val hidratosObjetivo: Int,
    val grasasConsumidas: Double,
    val grasasObjetivo: Int,
)

data class ObjetivosDiarios(
    val calorias: Int,
    val proteina: Int,
    val hidratos: Int,
    val grasas: Int,
)
