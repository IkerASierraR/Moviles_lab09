package com.example.locatioandroidsierra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.locatioandroidsierra.ui.components.MiMapa
import com.example.locatioandroidsierra.ui.screens.MapScreen
import com.example.locatioandroidsierra.ui.theme.LocatioAndroidSierraTheme
import com.example.locatioandroidsierra.viewmodel.MapViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocatioAndroidSierraTheme {
                    val mapViewModel = MapViewModel()
                    MapScreen(mapViewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LocatioAndroidSierraTheme {
        Greeting("Android")
    }
}