package com.example.appcaloriasia.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appcaloriasia.data.ObjetivoRepository
import com.example.appcaloriasia.viewmodel.ObjetivosViewModel
import com.example.appcaloriasia.viewmodel.ObjetivosViewModelFactory

@Composable
fun ObjetivosScreen() {
    val context = LocalContext.current
    val factory = ObjetivosViewModelFactory(ObjetivoRepository(context))
    val viewModel: ObjetivosViewModel = viewModel(factory = factory)
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(text = "Objetivos", style = MaterialTheme.typography.headlineMedium)

        CampoNumerico(
            etiqueta = "Kcal por día",
            valor = estado.calorias,
            onValorChange = viewModel::actualizarCalorias,
            error = if (estado.mostrarErrores) estado.errorCalorias else null,
        )
        CampoNumerico(
            etiqueta = "Proteína (g)",
            valor = estado.proteina,
            onValorChange = viewModel::actualizarProteina,
            error = if (estado.mostrarErrores) estado.errorProteina else null,
        )
        CampoNumerico(
            etiqueta = "Hidratos (g)",
            valor = estado.hidratos,
            onValorChange = viewModel::actualizarHidratos,
            error = if (estado.mostrarErrores) estado.errorHidratos else null,
        )
        CampoNumerico(
            etiqueta = "Grasas (g)",
            valor = estado.grasas,
            onValorChange = viewModel::actualizarGrasas,
            error = if (estado.mostrarErrores) estado.errorGrasas else null,
        )

        Button(
            onClick = viewModel::guardarObjetivos,
            enabled = estado.esValido,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Guardar objetivos")
        }

        if (estado.guardado) {
            Text(
                text = "Objetivos guardados",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun CampoNumerico(
    etiqueta: String,
    valor: String,
    onValorChange: (String) -> Unit,
    error: String?,
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(text = etiqueta) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        isError = error != null,
        supportingText = {
            if (error != null) {
                Text(text = error)
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(),
    )
}
