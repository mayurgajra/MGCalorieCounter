package com.mayurg.onboarding_presentation.goal

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.mayurg.core.R
import com.mayurg.core.domain.model.GoalType
import com.mayurg.core.util.UiEvent
import com.mayurg.core_ui.LocalSpacing
import com.mayurg.onboarding_presentation.components.ActionButton
import com.mayurg.onboarding_presentation.components.SelectableButton
import kotlinx.coroutines.flow.collect

@Composable
fun GoalScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: GoalViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceLarge)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.your_goal),
                style = MaterialTheme.typography.h3
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Row {
                SelectableButton(
                    text = stringResource(id = R.string.lose),
                    isSelected = viewModel.selectedGoal is GoalType.LooseWeight,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedTextColor = Color.White,
                    onClick = {
                        viewModel.onGoalTypeSelect(GoalType.LooseWeight)
                    },
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                SelectableButton(
                    text = stringResource(id = R.string.keep),
                    isSelected = viewModel.selectedGoal is GoalType.KeepWeight,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedTextColor = Color.White,
                    onClick = {
                        viewModel.onGoalTypeSelect(GoalType.KeepWeight)
                    },
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                SelectableButton(
                    text = stringResource(id = R.string.gain),
                    isSelected = viewModel.selectedGoal is GoalType.GainWeight,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedTextColor = Color.White,
                    onClick = {
                        viewModel.onGoalTypeSelect(GoalType.GainWeight)
                    },
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )

            }
        }

        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = viewModel::onNextClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }

}