package com.jdacodes.userdemo.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.jdacodes.userdemo.core.presentation.theme.getAppBarColor
import com.jdacodes.userdemo.core.presentation.theme.getTextColor

@Composable
fun HomeScreen(
    logout: () -> Unit,
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController
) {
    val navController = rememberNavController()

    var currentDestination by rememberSaveable { mutableStateOf(HomeDestinations.DASHBOARD) }

    val windowSize = with(LocalDensity.current) {
        currentWindowSize().toSize().toDpSize()
    }
    val layoutType = if (windowSize.width >= 1200.dp) {
        NavigationSuiteType.NavigationDrawer
    } else {
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
            currentWindowAdaptiveInfo()
        )
    }

    NavigationSuiteScaffold(
        layoutType = layoutType,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = getAppBarColor(),
            navigationBarContentColor = getTextColor(),
            navigationDrawerContainerColor = getAppBarColor(),
            navigationDrawerContentColor = getTextColor(),
            navigationRailContainerColor = getAppBarColor(),
            navigationRailContentColor = getTextColor()
        ),
        navigationSuiteItems = {
            HomeDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = stringResource(it.contentDescription)
                        )
                    },
                    label = { Text(stringResource(it.label)) },
                    selected = it == currentDestination,
                    onClick = {
                        navController.navigate(it.name) {
                            currentDestination = it
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                )
            }
        }
    ) {
        HomeNavGraph(
            navController = navController,
            logout = logout,
            snackbarHostState = snackbarHostState
        )
    }

}