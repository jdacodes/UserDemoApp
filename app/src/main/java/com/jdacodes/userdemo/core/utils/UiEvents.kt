package com.jdacodes.userdemo.core.utils

sealed class UiEvents {
    data class SnackBarEvent(val message: String) : UiEvents()
    data class NavigateEvent(val route: String) : UiEvents()
}