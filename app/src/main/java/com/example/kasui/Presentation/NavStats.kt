package com.example.kasui.Presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

enum class NavState {
    HOME,
    SEARCH,
    LIBRARY
}

object NavManager{
    private var _navStates : MutableStateFlow<NavState> = MutableStateFlow(NavState.HOME)
    val navStates: StateFlow<NavState> = _navStates

    fun changeNavState(state: NavState){
        _navStates.update { state }
    }

}