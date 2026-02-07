package com.example.appcaloriasia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appcaloriasia.data.ComidaEntity
import com.example.appcaloriasia.data.ComidaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HistorialViewModel(private val repository: ComidaRepository) : ViewModel() {
    val historial: StateFlow<List<ComidaEntity>> = repository.obtenerHistorial()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val agrupadoPorFecha: StateFlow<Map<String, List<ComidaEntity>>> = historial
        .map { comidas -> comidas.groupBy { it.fecha } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())
}

class HistorialViewModelFactory(
    private val repository: ComidaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistorialViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistorialViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
