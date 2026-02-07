package com.example.appcaloriasia.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appcaloriasia.data.ComidaRepository
import com.example.appcaloriasia.viewmodel.HistorialViewModel
import com.example.appcaloriasia.viewmodel.HistorialViewModelFactory

@Composable
fun HistorialScreen() {
    val context = LocalContext.current
    val factory = HistorialViewModelFactory(ComidaRepository(context))
    val viewModel: HistorialViewModel = viewModel(factory = factory)
    val agrupado by viewModel.agrupadoPorFecha.collectAsStateWithLifecycle()
    val fechasOrdenadas = agrupado.keys.sortedDescending()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(text = "Historial", style = MaterialTheme.typography.headlineMedium)
        if (agrupado.isEmpty()) {
            Text(
                text = "Aún no hay comidas registradas.",
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                fechasOrdenadas.forEach { fecha ->
                    val comidas = agrupado[fecha].orEmpty()
                    item {
                        Text(text = fecha, style = MaterialTheme.typography.titleMedium)
                    }
                    items(comidas) { comida ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            ),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = comida.nombre, style = MaterialTheme.typography.titleMedium)
                                Text(text = comida.marca, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = "Kcal: ${comida.calorias.toInt()} · P ${comida.proteina.toInt()}g " +
                                        "C ${comida.hidratos.toInt()}g · G ${comida.grasas.toInt()}g",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
