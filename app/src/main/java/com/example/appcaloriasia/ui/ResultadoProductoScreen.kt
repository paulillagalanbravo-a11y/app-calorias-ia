package com.example.appcaloriasia.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appcaloriasia.data.ComidaRepository
import com.example.appcaloriasia.data.OpenFoodFactsRepository
import com.example.appcaloriasia.viewmodel.ResultadoProductoViewModel
import com.example.appcaloriasia.viewmodel.ResultadoProductoViewModelFactory

@Composable
fun ResultadoProductoScreen(
    codigoBarras: String,
    navController: NavController,
) {
    val context = LocalContext.current
    val factory = ResultadoProductoViewModelFactory(
        codigoBarras = codigoBarras,
        openFoodFactsRepository = OpenFoodFactsRepository(),
        comidaRepository = ComidaRepository(context),
    )
    val viewModel: ResultadoProductoViewModel = viewModel(factory = factory)
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(text = "Resultado", style = MaterialTheme.typography.headlineMedium)

        when {
            estado.cargando -> {
                Text(text = "Buscando producto...", style = MaterialTheme.typography.bodyMedium)
            }
            estado.error != null -> {
                Text(text = estado.error ?: "Error", style = MaterialTheme.typography.bodyMedium)
                OutlinedButton(onClick = viewModel::buscarProducto) {
                    Text(text = "Reintentar")
                }
            }
            estado.producto != null -> {
                val producto = estado.producto
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = producto.nombre, style = MaterialTheme.typography.titleLarge)
                        Text(text = "Marca: ${producto.marca}")
                        Text(text = "Porción: ${producto.porcion}")
                        if (producto.tieneNutrientes) {
                            Text(text = "Kcal: ${producto.calorias.toInt()}")
                            Text(
                                text = "Macros: P ${producto.proteina.toInt()}g · " +
                                    "C ${producto.hidratos.toInt()}g · G ${producto.grasas.toInt()}g",
                            )
                        } else {
                            Text(text = "Sin datos nutricionales")
                        }
                        Text(text = "Ingredientes: ${producto.ingredientes}")
                    }
                }

                Button(
                    onClick = {
                        viewModel.guardarProducto()
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Guardar en el día")
                }

                if (estado.guardado) {
                    Text(
                        text = "Producto guardado en el historial",
                        color = MaterialTheme.colorScheme.primary,
                    )
                    OutlinedButton(
                        onClick = { navController.navigate("historial") },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = "Ver historial")
                    }
                }
            }
        }
    }
}
