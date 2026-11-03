package pl.wieliszew.smartcus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as SmartCusApplication
        val repository = app.repository

        setContent {
            SmartCusApp(repository = repository)
        }
    }
}

