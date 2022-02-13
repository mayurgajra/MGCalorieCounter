package com.mgcaloriecounter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.common.truth.Truth.assertThat
import com.mayurg.core.domain.model.ActivityLevel
import com.mayurg.core.domain.model.Gender
import com.mayurg.core.domain.model.GoalType
import com.mayurg.core.domain.model.UserInfo
import com.mayurg.core.domain.preferences.Preferences
import com.mayurg.core.domain.use_case.FilterOutDigits
import com.mayurg.tracker_domain.model.TrackableFood
import com.mayurg.tracker_domain.use_case.*
import com.mayurg.tracker_presentation.search.SearchScreen
import com.mayurg.tracker_presentation.search.SearchViewModel
import com.mayurg.tracker_presentation.tracker_overview.TrackerOverViewScreen
import com.mayurg.tracker_presentation.tracker_overview.TrackerOverviewViewModel
import com.mgcaloriecounter.navigation.Route
import com.mgcaloriecounter.repository.TrackerRepositoryFake
import com.mgcaloriecounter.ui.theme.MGCalorieCounterTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt

@HiltAndroidTest
class TrackerOverviewE2E {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var trackerRepositoryFake: TrackerRepositoryFake
    private lateinit var trackerUseCases: TrackerUseCases
    private lateinit var preferences: Preferences
    private lateinit var trackerOverviewViewModel: TrackerOverviewViewModel
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Male,
            age = 20,
            weight = 80f,
            height = 180,
            activityLevel = ActivityLevel.Medium,
            goalType = GoalType.KeepWeight,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f,
        )
        trackerRepositoryFake = TrackerRepositoryFake()
        trackerUseCases = TrackerUseCases(
            trackFood = TrackFood(trackerRepositoryFake),
            searchFood = SearchFood(trackerRepositoryFake),
            getFoodsForDate = GetFoodsForDate(trackerRepositoryFake),
            deleteTrackedFood = DeleteTrackedFood(trackerRepositoryFake),
            calculateMealNutrients = CalculateMealNutrients(preferences)
        )
        trackerOverviewViewModel = TrackerOverviewViewModel(
            preferences = preferences,
            trackerUseCases = trackerUseCases
        )
        searchViewModel = SearchViewModel(
            trackerUseCases = trackerUseCases,
            filterOutDigits = FilterOutDigits()
        )

        composeRule.setContent {
            MGCalorieCounterTheme {
                val scaffoldState = rememberScaffoldState()
                navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Route.TRACKER_OVERVIEW
                    ) {
                        composable(Route.TRACKER_OVERVIEW) {
                            TrackerOverViewScreen(
                                onNavigateToSearch = { mealName, day, month, year ->
                                    navController.navigate(
                                        Route.SEARCH + "/$mealName" + "/$day" + "/$month" + "/$year"
                                    )
                                },
                                viewModel = trackerOverviewViewModel
                            )
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
                                },
                                viewModel = searchViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun addBreakfast_appearsUnderBreakfast_nutrientsProperlyCalculated() {
        trackerRepositoryFake.searchResults = listOf(
            TrackableFood(
                name = "banana",
                imageUrl = null,
                caloriesPer100g = 150,
                carbsPer100g = 50,
                proteinPer100g = 5,
                fatPer100g = 1
            )
        )
        val addedAmount = 150
        val expectedCalories = (1.5f * 150).roundToInt()
        val expectedCarbs = (1.5f * 50).roundToInt()
        val expectedProtein = (1.5f * 5).roundToInt()
        val expectedFat = (1.5f * 1).roundToInt()

        composeRule
            .onNodeWithText("Add Breakfast")
            .assertDoesNotExist()

        composeRule
            .onNodeWithContentDescription("Breakfast")
            .performClick()


        composeRule
            .onNodeWithText("Add Breakfast")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Add Breakfast")
            .performClick()

        assertThat(
            navController.currentDestination?.route?.startsWith(Route.SEARCH)
        ).isTrue()


        composeRule
            .onNodeWithTag("search_textfield")
            .performTextInput("banana")


        composeRule
            .onNodeWithContentDescription("Search...")
            .performClick()

        composeRule
            .onNodeWithText("Carbs")
            .performClick()


        composeRule
            .onNodeWithContentDescription("Amount")
            .performTextInput(addedAmount.toString())

        composeRule
            .onNodeWithContentDescription("Track")
            .performClick()

        assertThat(
            navController.currentDestination?.route?.startsWith(Route.TRACKER_OVERVIEW)
        ).isTrue()

        composeRule
            .onAllNodesWithText(expectedCarbs.toString())
            .onFirst()
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText(expectedProtein.toString())
            .onFirst()
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText(expectedFat.toString())
            .onFirst()
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText(expectedCalories.toString())
            .onFirst()
            .assertIsDisplayed()


    }
}