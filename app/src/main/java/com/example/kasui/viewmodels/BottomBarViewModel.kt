package com.example.kasui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kasui.Presentation.NavManager
import com.example.kasui.Presentation.NavRoutes
import com.example.kasui.Presentation.NavState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BottomBarViewModel : ViewModel() {
    // EXTERNAL
    lateinit var navState: StateFlow<NavRoutes>

    // INTERNAL

    init {
        viewModelScope.launch {
            navState = NavManager.navStates
        }
    }

    fun onChangeNavState(navState: NavRoutes) {
        viewModelScope.launch {
            NavManager.changeNavState(navState)
        }
    }

}