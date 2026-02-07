package com.example.appcaloriasia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appcaloriasia.data.Objetivo
import com.example.appcaloriasia.data.ObjetivoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ObjetivosViewModel(private val repository: ObjetivoRepository) : ViewModel() {
    private val _estado = MutableStateFlow(ObjetivosUiState())
    val estado: StateFlow<ObjetivosUiState> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            repository.obtenerObjetivo().collectLatest { objetivo ->
                if (objetivo != null) {
                    actualizarEstado(
                        calorias = objetivo.calorias.toString(),
                        proteina = objetivo.proteina.toString(),
                        hidratos = objetivo.hidratos.toString(),
                        grasas = objetivo.grasas.toString(),
                        guardado = true,
                        mostrarErrores = false,
                    )
                }
            }
        }
    }

    fun actualizarCalorias(valor: String) {
        actualizarEstado(calorias = valor, mostrarErrores = true)
    }

    fun actualizarProteina(valor: String) {
        actualizarEstado(proteina = valor, mostrarErrores = true)
    }

    fun actualizarHidratos(valor: String) {
        actualizarEstado(hidratos = valor, mostrarErrores = true)
    }

    fun actualizarGrasas(valor: String) {
        actualizarEstado(grasas = valor, mostrarErrores = true)
    }

    fun guardarObjetivos() {
        if (!_estado.value.esValido) {
            _estado.value = _estado.value.copy(mostrarErrores = true)
            return
        }
        val calorias = _estado.value.calorias.toIntOrNull() ?: 0
        val proteina = _estado.value.proteina.toIntOrNull() ?: 0
        val hidratos = _estado.value.hidratos.toIntOrNull() ?: 0
        val grasas = _estado.value.grasas.toIntOrNull() ?: 0

        viewModelScope.launch {
            repository.guardarObjetivo(
                Objetivo(
                    calorias = calorias,
                    proteina = proteina,
                    hidratos = hidratos,
                    grasas = grasas,
                ),
            )
            _estado.value = _estado.value.copy(guardado = true, mostrarErrores = false)
        }
    }

    private fun actualizarEstado(
        calorias: String = _estado.value.calorias,
        proteina: String = _estado.value.proteina,
        hidratos: String = _estado.value.hidratos,
        grasas: String = _estado.value.grasas,
        guardado: Boolean = false,
        mostrarErrores: Boolean = _estado.value.mostrarErrores,
    ) {
        val errorCalorias = validarCampo(calorias)
        val errorProteina = validarCampo(proteina)
        val errorHidratos = validarCampo(hidratos)
        val errorGrasas = validarCampo(grasas)
        val esValido = listOf(errorCalorias, errorProteina, errorHidratos, errorGrasas).all { it == null }

        _estado.value = _estado.value.copy(
            calorias = calorias,
            proteina = proteina,
            hidratos = hidratos,
            grasas = grasas,
            errorCalorias = errorCalorias,
            errorProteina = errorProteina,
            errorHidratos = errorHidratos,
            errorGrasas = errorGrasas,
            esValido = esValido,
            guardado = guardado,
            mostrarErrores = mostrarErrores,
        )
    }

    private fun validarCampo(valor: String): String? {
        if (valor.isBlank()) return "Campo obligatorio"
        val numero = valor.toIntOrNull() ?: return "Ingresa un número válido"
        if (numero < 0) return "No se permiten valores negativos"
        return null
    }
}

class ObjetivosViewModelFactory(
    private val repository: ObjetivoRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ObjetivosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ObjetivosViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}

data class ObjetivosUiState(
    val calorias: String = "",
    val proteina: String = "",
    val hidratos: String = "",
    val grasas: String = "",
    val guardado: Boolean = false,
    val esValido: Boolean = false,
    val mostrarErrores: Boolean = false,
    val errorCalorias: String? = null,
    val errorProteina: String? = null,
    val errorHidratos: String? = null,
    val errorGrasas: String? = null,
)
