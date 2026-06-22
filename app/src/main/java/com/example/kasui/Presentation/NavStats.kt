package com.example.kasui.Presentation

enum class NavStates {
    HOME,
    SEARCH,
    LIBRARY
}

object NavManager{
    var navState : NavStates = NavStates.HOME

}