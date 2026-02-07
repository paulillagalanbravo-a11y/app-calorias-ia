package com.example.appcaloriasia.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegistrarComidaScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Registrar comida",
            style = MaterialTheme.typography.headlineMedium,
        )
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
        ) {
            Text(text = "Foto", style = MaterialTheme.typography.titleMedium)
        }
        Button(
            onClick = { navController.navigate("escanear") },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
        ) {
            Text(text = "Código de barras", style = MaterialTheme.typography.titleMedium)
        }
    }
}
