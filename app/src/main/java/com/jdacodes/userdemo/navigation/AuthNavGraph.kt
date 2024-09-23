package com.jdacodes.userdemo.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jdacodes.userdemo.auth.presentation.login.LoginScreen
import com.jdacodes.userdemo.auth.presentation.login.LoginViewModel
import com.jdacodes.userdemo.auth.presentation.register.RegisterScreen
import com.jdacodes.userdemo.auth.presentation.register.RegisterViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.AuthNav(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    navigation(
        startDestination = ScreenRoutes.LoginScreen.route,
        route = ScreenRoutes.AuthNav.route
    ) {
        composable(route = ScreenRoutes.LoginScreen.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val scope = rememberCoroutineScope()
            Log.d("AuthNav", "Navigating to LoginScreen")

            LoginScreen(
                viewModel = viewModel,
                uiState = uiState,
                onLoginSuccess = { message ->
                    navController.popBackStack()
                    navController.navigate(ScreenRoutes.HomeNav.route) {
                        popUpTo(ScreenRoutes.LoginScreen.route) { inclusive = true }
                    }
                    scope.launch {
                        Log.d(
                            "AuthNav",
                            "Showing snackbar on login success with message: $message"
                        )
                        snackbarHostState.showSnackbar(message)
                    }

                },
                onLoginFailure = { message ->
                    scope.launch {
                        Log.d(
                            "AuthNav",
                            "Showing snackbar on login failure with message: $message"
                        )
                        snackbarHostState.showSnackbar(message)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                snackbarHostState = snackbarHostState,
                onClickDontHaveAccount = { navController.navigate(ScreenRoutes.RegisterScreen.route)},
                onClickForgotPassword = {},

                )
        }
        //Add other composables for AuthNav

        composable(route = ScreenRoutes.RegisterScreen.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val scope = rememberCoroutineScope()
            Log.d("AuthNav", "Navigating to RegisterScreen")

            RegisterScreen(
                viewModel = viewModel,
                uiState = uiState,
                onRegisterSuccess = { message ->
                    navController.popBackStack()
                    navController.navigate(ScreenRoutes.HomeNav.route) {
                        popUpTo(ScreenRoutes.RegisterScreen.route) { inclusive = true }
                    }
                    scope.launch {
                        Log.d(
                            "AuthNav",
                            "Showing snackbar on login success with message: $message"
                        )
                        snackbarHostState.showSnackbar(message)
                    }

                },
                onRegisterFailure = { message ->
                    scope.launch {
                        Log.d(
                            "AuthNav",
                            "Showing snackbar on login failure with message: $message"
                        )
                        snackbarHostState.showSnackbar(message)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                snackbarHostState = snackbarHostState,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}

