package com.dscvit.werk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.dscvit.werk.ui.auth.AuthScreen
import com.dscvit.werk.ui.theme.WerkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WerkApp()
        }
    }
}

@Composable
fun WerkApp() {
    WerkTheme {
        Scaffold {
            AuthScreen()
        }
    }
}
