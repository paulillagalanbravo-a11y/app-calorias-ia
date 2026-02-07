package com.example.appcaloriasia.data

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class OpenFoodFactsRepository {
    private val client = OkHttpClient()

    suspend fun buscarProducto(codigoBarras: String): ResultadoProducto {
        val url = "https://world.openfoodfacts.org/api/v2/product/$codigoBarras.json"
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Respuesta inválida")
        }
        val body = response.body?.string() ?: throw IOException("Respuesta vacía")
        val json = JSONObject(body)
        val status = json.optInt("status", 0)
        if (status == 0) {
            return ResultadoProducto.NoEncontrado
        }
        val product = json.optJSONObject("product") ?: return ResultadoProducto.NoEncontrado
        val nombre = product.optString("product_name", "Producto sin nombre")
        val marca = product.optString("brands", "Marca no disponible")
        val porcion = product.optString("serving_size", "Porción no disponible")
        val ingredientes = product.optString("ingredients_text", "Sin ingredientes")

        val nutriments = product.optJSONObject("nutriments")
        val calorias = nutriments?.optDouble("energy-kcal_100g")
            ?.takeIf { !it.isNaN() } ?: nutriments?.optDouble("energy-kcal") ?: 0.0
        val proteina = nutriments?.optDouble("proteins_100g")?.takeIf { !it.isNaN() } ?: 0.0
        val hidratos = nutriments?.optDouble("carbohydrates_100g")?.takeIf { !it.isNaN() } ?: 0.0
        val grasas = nutriments?.optDouble("fat_100g")?.takeIf { !it.isNaN() } ?: 0.0

        val tieneNutrientes = nutriments != null &&
            listOf(calorias, proteina, hidratos, grasas).any { it > 0.0 }

        return ResultadoProducto.Exito(
            ProductoAlimenticio(
                codigoBarras = codigoBarras,
                nombre = nombre.ifBlank { "Producto sin nombre" },
                marca = marca.ifBlank { "Marca no disponible" },
                porcion = porcion.ifBlank { "Porción no disponible" },
                calorias = calorias,
                proteina = proteina,
                hidratos = hidratos,
                grasas = grasas,
                ingredientes = ingredientes.ifBlank { "Sin ingredientes" },
                tieneNutrientes = tieneNutrientes,
            ),
        )
    }
}

sealed class ResultadoProducto {
    data class Exito(val producto: ProductoAlimenticio) : ResultadoProducto()
    data object NoEncontrado : ResultadoProducto()
}

data class ProductoAlimenticio(
    val codigoBarras: String,
    val nombre: String,
    val marca: String,
    val porcion: String,
    val calorias: Double,
    val proteina: Double,
    val hidratos: Double,
    val grasas: Double,
    val ingredientes: String,
    val tieneNutrientes: Boolean,
)
