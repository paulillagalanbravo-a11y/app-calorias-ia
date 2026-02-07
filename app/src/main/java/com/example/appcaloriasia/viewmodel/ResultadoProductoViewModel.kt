package com.example.appcaloriasia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appcaloriasia.data.ComidaEntity
import com.example.appcaloriasia.data.ComidaRepository
import com.example.appcaloriasia.data.OpenFoodFactsRepository
import com.example.appcaloriasia.data.ProductoAlimenticio
import com.example.appcaloriasia.data.ResultadoProducto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate

class ResultadoProductoViewModel(
    private val codigoBarras: String,
    private val openFoodFactsRepository: OpenFoodFactsRepository,
    private val comidaRepository: ComidaRepository,
) : ViewModel() {
    private val _estado = MutableStateFlow(ResultadoProductoUiState(cargando = true))
    val estado: StateFlow<ResultadoProductoUiState> = _estado.asStateFlow()

    init {
        buscarProducto()
    }

    fun buscarProducto() {
        _estado.value = ResultadoProductoUiState(cargando = true)
        viewModelScope.launch {
            try {
                when (val resultado = openFoodFactsRepository.buscarProducto(codigoBarras)) {
                    is ResultadoProducto.Exito -> {
                        _estado.value = ResultadoProductoUiState(producto = resultado.producto)
                    }
                    ResultadoProducto.NoEncontrado -> {
                        _estado.value = ResultadoProductoUiState(error = "Producto no encontrado")
                    }
                }
            } catch (error: IOException) {
                _estado.value = ResultadoProductoUiState(error = "Sin conexión. Intenta de nuevo.")
            } catch (error: Exception) {
                _estado.value = ResultadoProductoUiState(error = "Ocurrió un error inesperado")
            }
        }
    }

    fun guardarProducto() {
        val producto = _estado.value.producto ?: return
        viewModelScope.launch {
            comidaRepository.guardarComida(
                ComidaEntity(
                    codigoBarras = producto.codigoBarras,
                    nombre = producto.nombre,
                    marca = producto.marca,
                    porcion = producto.porcion,
                    calorias = producto.calorias,
                    proteina = producto.proteina,
                    hidratos = producto.hidratos,
                    grasas = producto.grasas,
                    ingredientes = producto.ingredientes,
                    fecha = LocalDate.now().toString(),
                ),
            )
            _estado.value = _estado.value.copy(guardado = true)
        }
    }
}

class ResultadoProductoViewModelFactory(
    private val codigoBarras: String,
    private val openFoodFactsRepository: OpenFoodFactsRepository,
    private val comidaRepository: ComidaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultadoProductoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResultadoProductoViewModel(codigoBarras, openFoodFactsRepository, comidaRepository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}

data class ResultadoProductoUiState(
    val cargando: Boolean = false,
    val producto: ProductoAlimenticio? = null,
    val error: String? = null,
    val guardado: Boolean = false,
)
