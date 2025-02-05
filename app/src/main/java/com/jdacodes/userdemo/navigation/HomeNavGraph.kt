package com.jdacodes.userdemo.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jdacodes.userdemo.dashboard.presentation.DashboardScreen
import com.jdacodes.userdemo.dashboard.presentation.DashboardViewModel
import com.jdacodes.userdemo.profile.presentation.account.ProfileScreen
import com.jdacodes.userdemo.profile.presentation.account.ProfileViewModel
import com.jdacodes.userdemo.profile.presentation.account.UpdateProfileScreen
import com.jdacodes.userdemo.userlist.presentation.UserScreen
import com.jdacodes.userdemo.userlist.presentation.UserViewModel
import com.jdacodes.userdemo.userlist.presentation.composables.UserDetailScreen
import kotlinx.coroutines.launch

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    logout: () -> Unit,
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController
) {
    NavHost(
        navController = navController,
        route = ScreenRoutes.HomeNav.route,
        startDestination = ScreenRoutes.DashboardScreen.route
    ) {
        composable(route = ScreenRoutes.DashboardScreen.route) {
            val viewModel: DashboardViewModel = hiltViewModel()
            val uiState by viewModel.uiListState.collectAsStateWithLifecycle()
            DashboardScreen(
                viewModel = viewModel,
                uiState = uiState,
                keyboardController = keyboardController,
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        composable(route = ScreenRoutes.UsersScreen.route) {
            val viewModel: UserViewModel = hiltViewModel()
            val uiState by viewModel.uiListState.collectAsStateWithLifecycle()
            UserScreen(
                viewModel = viewModel,
                uiState = uiState,
                snackbarHostState = snackbarHostState,
                onClickUser = { userId ->
                    navController.navigate("${ScreenRoutes.UserDetailScreen.route}/$userId")
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            )
        }

        composable(
            route = ScreenRoutes.UserDetailScreen.fullRoute
        ) { backStackEntry ->
            val viewModel: UserViewModel = hiltViewModel()
            val uiState by viewModel.uiDetailState.collectAsStateWithLifecycle()
            val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0

            UserDetailScreen(
                viewModel = viewModel,
                uiState = uiState,
                userId = userId,
                onClickBack = { navController.navigateUp() }
            )
        }

        composable(route = ScreenRoutes.ProfileScreen.route) {
            val viewModel: ProfileViewModel = hiltViewModel()
            val scope = rememberCoroutineScope()
            ProfileScreen(
                viewModel = viewModel,
                onLogoutSuccess = { message ->
                    logout()
                    scope.launch {
                        Log.d(
                            "HomeNavGraph",
                            "Showing snackbar on logout success with message: $message"
                        )
                        snackbarHostState.showSnackbar(message)
                    }

                },
                onLogoutFailure = { message ->
                    scope.launch {
                        Log.d(
                            "HomeNavGraph",
                            "Showing snackbar on logout failure with message: $message"
                        )
                        snackbarHostState.showSnackbar(message)
                    }
                },
                onClickUpdateProfile = { userId ->
                    navController.navigate("${ScreenRoutes.UpdateProfileScreen.route}/$userId")
                },
                onDeleteSuccess = { message ->

                    scope.launch {
                        Log.d(
                            "HomeNavGraph",
                            "Showing snackbar on delete success with message: $message"
                        )
                        snackbarHostState.showSnackbar(message)
                    }
                    logout()
                },
                onDeleteFailure = { message ->
                    scope.launch {
                        Log.d(
                            "HomeNavGraph",
                            "Showing snackbar on delete failure with message: $message"
                        )
                        snackbarHostState.showSnackbar(message)
                    }
                },
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),

                )
        }
        composable(
            route = ScreenRoutes.UpdateProfileScreen.fullRoute
        ) { backStackEntry ->
            val viewModel: ProfileViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0
            val scope = rememberCoroutineScope()

            UpdateProfileScreen(
                viewModel = viewModel,
                uiState = uiState,
                userId = userId,
                onUpdateSuccess = { message ->
                    scope.launch {
                        Log.d(
                            "HomeNavGraph",
                            "Showing snackbar on logout failure with message: $message"
                        )
                        snackbarHostState.showSnackbar(message)
                        navController.navigateUp()
                    }
                },
                onUpdateFailure = { message ->
                    scope.launch {
                        Log.d(
                            "HomeNavGraph",
                            "Showing snackbar on logout failure with message: $message"
                        )
                        snackbarHostState.showSnackbar(message)
                    }
                },
                onClickBack = { navController.navigateUp() },
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                keyboardController = keyboardController
            )
        }
    }
}

