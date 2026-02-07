package com.example.appcaloriasia.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appcaloriasia.viewmodel.InicioViewModel

@Composable
fun InicioScreen(viewModel: InicioViewModel) {
    val resumen by viewModel.resumen.collectAsStateWithLifecycle()
    val kcalRestantes = (resumen.caloriasObjetivo - resumen.caloriasConsumidas).coerceAtLeast(0.0)
    val proteinaRestante = (resumen.proteinaObjetivo - resumen.proteinaConsumida).coerceAtLeast(0.0)
    val hidratosRestantes = (resumen.hidratosObjetivo - resumen.hidratosConsumidos).coerceAtLeast(0.0)
    val grasasRestantes = (resumen.grasasObjetivo - resumen.grasasConsumidas).coerceAtLeast(0.0)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = viewModel.titulo,
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "App de análisis nutricional",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Resumen del día",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Kcal consumidas: ${resumen.caloriasConsumidas.toInt()}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Kcal restantes: ${kcalRestantes.toInt()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                ProgresoFila(
                    etiqueta = "Kcal",
                    valor = resumen.caloriasConsumidas,
                    objetivo = resumen.caloriasObjetivo,
                )
                DetalleMacro(
                    etiqueta = "Proteína",
                    consumidas = resumen.proteinaConsumida,
                    restantes = proteinaRestante,
                )
                ProgresoFila(
                    etiqueta = "Proteína",
                    valor = resumen.proteinaConsumida,
                    objetivo = resumen.proteinaObjetivo,
                    unidad = "g",
                )
                DetalleMacro(
                    etiqueta = "Hidratos",
                    consumidas = resumen.hidratosConsumidos,
                    restantes = hidratosRestantes,
                )
                ProgresoFila(
                    etiqueta = "Hidratos",
                    valor = resumen.hidratosConsumidos,
                    objetivo = resumen.hidratosObjetivo,
                    unidad = "g",
                )
                DetalleMacro(
                    etiqueta = "Grasas",
                    consumidas = resumen.grasasConsumidas,
                    restantes = grasasRestantes,
                )
                ProgresoFila(
                    etiqueta = "Grasas",
                    valor = resumen.grasasConsumidas,
                    objetivo = resumen.grasasObjetivo,
                    unidad = "g",
                )
            }
        }
    }
}

@Composable
private fun DetalleMacro(
    etiqueta: String,
    consumidas: Double,
    restantes: Double,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "$etiqueta consumidos: ${consumidas.toInt()} g",
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = "Restantes: ${restantes.toInt()} g",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun ProgresoFila(
    etiqueta: String,
    valor: Double,
    objetivo: Int,
    unidad: String = "kcal",
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = etiqueta, style = MaterialTheme.typography.bodyMedium)
            Text(text = "${valor.toInt()}/$objetivo $unidad", style = MaterialTheme.typography.bodyMedium)
        }
        LinearProgressIndicator(
            progress = if (objetivo > 0) (valor / objetivo).toFloat() else 0f,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}
