package com.example.appcaloriasia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.appcaloriasia.ui.AppNavHost
import com.example.appcaloriasia.ui.theme.AppCaloriasIATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppCaloriasIATheme {
                Surface {
                    AppNavHost()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppPreview() {
    AppCaloriasIATheme {
        Surface {
            AppNavHost()
        }
    }
}
