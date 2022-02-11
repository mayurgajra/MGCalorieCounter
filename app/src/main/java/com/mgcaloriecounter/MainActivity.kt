package com.mgcaloriecounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mayurg.core.domain.preferences.Preferences
import com.mayurg.core.navigation.Route
import com.mayurg.onboarding_presentation.activity.ActivityScreen
import com.mayurg.onboarding_presentation.age.AgeScreen
import com.mayurg.onboarding_presentation.gender.GenderScreen
import com.mayurg.onboarding_presentation.goal.GoalScreen
import com.mayurg.onboarding_presentation.height.HeightScreen
import com.mayurg.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.mayurg.onboarding_presentation.weight.WeightScreen
import com.mayurg.onboarding_presentation.welcome.WelcomeScreen
import com.mayurg.tracker_presentation.search.SearchScreen
import com.mayurg.tracker_presentation.tracker_overview.TrackerOverViewScreen
import com.mgcaloriecounter.navigation.navigate
import com.mgcaloriecounter.ui.theme.MGCalorieCounterTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shouldShowOnboarding = preferences.loadShouldShowOnboarding()
        setContent {
            MGCalorieCounterTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = if (shouldShowOnboarding) {
                            Route.WELCOME
                        } else Route.TRACKER_OVERVIEW
                    ) {
                        composable(route = Route.WELCOME) {
                            WelcomeScreen(onNavigate = navController::navigate)
                        }

                        composable(Route.GENDER) {
                            GenderScreen(onNavigate = navController::navigate)
                        }

                        composable(Route.AGE) {
                            AgeScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = navController::navigate
                            )
                        }

                        composable(Route.HEIGHT) {
                            HeightScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = navController::navigate
                            )
                        }

                        composable(Route.WEIGHT) {
                            WeightScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = navController::navigate
                            )
                        }

                        composable(Route.ACTIVITY) {
                            ActivityScreen(onNavigate = navController::navigate)
                        }

                        composable(Route.GOAL) {
                            GoalScreen(onNavigate = navController::navigate)
                        }

                        composable(Route.NUTRIENT_GOAL) {
                            NutrientGoalScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = navController::navigate
                            )
                        }

                        composable(Route.TRACKER_OVERVIEW) {
                            TrackerOverViewScreen(onNavigate = navController::navigate)
                        }

                        composable(
                            route = Route.SEARCH + "/{mealName}/{dayOfMonth}/{month}/{year}",
                            arguments = listOf(
                                navArgument("mealName") {
                                    type = NavType.StringType
                                },
                                navArgument("dayOfMonth") {
                                    type = NavType.IntType
                                },
                                navArgument("month") {
                                    type = NavType.IntType
                                },
                                navArgument("year") {
                                    type = NavType.IntType
                                }
                            )
                        ) {
                            val mealName = it.arguments?.getString("mealName")!!
                            val dayOfMonth = it.arguments?.getInt("dayOfMonth")!!
                            val month = it.arguments?.getInt("month")!!
                            val year = it.arguments?.getInt("year")!!
                            SearchScreen(
                                scaffoldState = scaffoldState,
                                mealName = mealName,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navController.navigateUp()
                                })
                        }
                    }
                }

            }
        }
    }
}
