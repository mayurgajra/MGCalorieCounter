package com.mgcaloriecounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mayurg.core.navigation.Route
import com.mayurg.onboarding_presentation.welcome.WelcomeScreen
import com.mgcaloriecounter.navigation.navigate
import com.mgcaloriecounter.ui.theme.MGCalorieCounterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MGCalorieCounterTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Route.WELCOME
                ){
                    composable(route = Route.WELCOME){
                        WelcomeScreen(onNavigate = navController::navigate)
                    }
                }
            }
        }
    }
}
