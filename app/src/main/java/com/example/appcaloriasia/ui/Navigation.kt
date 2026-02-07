package com.example.appcaloriasia.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.appcaloriasia.viewmodel.InicioViewModel
import com.example.appcaloriasia.viewmodel.InicioViewModelFactory
import com.example.appcaloriasia.data.ComidaRepository
import com.example.appcaloriasia.data.ObjetivoRepository

private const val ROUTE_INICIO = "inicio"
private const val ROUTE_REGISTRAR = "registrar"
private const val ROUTE_OBJETIVOS = "objetivos"
private const val ROUTE_HISTORIAL = "historial"
private const val ROUTE_ESCANEAR = "escanear"
private const val ROUTE_RESULTADO = "resultado"

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val items = listOf(
        NavItem("Hoy", ROUTE_INICIO, Icons.Filled.Home),
        NavItem("Registrar", ROUTE_REGISTRAR, Icons.Filled.Restaurant),
        NavItem("Objetivos", ROUTE_OBJETIVOS, Icons.Filled.Flag),
        NavItem("Historial", ROUTE_HISTORIAL, Icons.Filled.History),
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = items.any { it.route == currentRoute }
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(ROUTE_INICIO) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { androidx.compose.material3.Icon(item.icon, contentDescription = item.label) },
                            label = { Text(text = item.label) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROUTE_INICIO,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = ROUTE_INICIO) {
                val factory = InicioViewModelFactory(
                    comidaRepository = ComidaRepository(context),
                    objetivoRepository = ObjetivoRepository(context),
                )
                val viewModel: InicioViewModel = viewModel(factory = factory)
                InicioScreen(viewModel = viewModel)
            }
            composable(route = ROUTE_REGISTRAR) {
                RegistrarComidaScreen(navController)
            }
            composable(route = ROUTE_OBJETIVOS) {
                ObjetivosScreen()
            }
            composable(route = ROUTE_HISTORIAL) {
                HistorialScreen()
            }
            composable(route = ROUTE_ESCANEAR) {
                BarcodeScannerScreen(
                    onCodigoDetectado = { codigo ->
                        navController.navigate("$ROUTE_RESULTADO/$codigo") {
                            launchSingleTop = true
                        }
                    },
                )
            }
            composable(route = "$ROUTE_RESULTADO/{codigoBarras}") { backStackEntry ->
                val codigoBarras = backStackEntry.arguments?.getString("codigoBarras") ?: ""
                ResultadoProductoScreen(codigoBarras = codigoBarras, navController = navController)
            }
        }
    }
}

private data class NavItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)
