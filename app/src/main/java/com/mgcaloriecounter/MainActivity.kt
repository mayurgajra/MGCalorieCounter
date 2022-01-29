package com.mgcaloriecounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mayurg.core.navigation.Route
import com.mayurg.onboarding_presentation.gender.GenderScreen
import com.mayurg.onboarding_presentation.welcome.WelcomeScreen
import com.mgcaloriecounter.navigation.navigate
import com.mgcaloriecounter.ui.theme.MGCalorieCounterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MGCalorieCounterTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Route.WELCOME
                ) {
                    composable(route = Route.WELCOME) {
                        WelcomeScreen(onNavigate = navController::navigate)
                    }

                    composable(Route.GENDER) {
                        GenderScreen(onNavigate = navController::navigate)
                    }
                }
            }
        }
    }
}
