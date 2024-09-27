package com.jdacodes.userdemo.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun RootNav(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController,
    startDestination: String,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        AuthNav(
            navController = navController,
            snackbarHostState = snackbarHostState,
            keyboardController = keyboardController
        )

        composable(route = ScreenRoutes.HomeNav.route) {
            HomeScreen(
                logout = {
                    navController.navigate(ScreenRoutes.AuthNav.route) {
                        popUpTo(0) {}
                    }
                },
                snackbarHostState = snackbarHostState,
                keyboardController = keyboardController
            )
        }
    }
}
