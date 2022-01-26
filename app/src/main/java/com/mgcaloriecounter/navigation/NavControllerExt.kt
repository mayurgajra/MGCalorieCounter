package com.mgcaloriecounter.navigation

import androidx.navigation.NavController
import com.mayurg.core.util.UiEvent

fun NavController.navigate(uiEvent: UiEvent.Navigate){
    this.navigate(uiEvent.route)
}