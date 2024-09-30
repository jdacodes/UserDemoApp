package com.jdacodes.userdemo.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.jdacodes.userdemo.R
import com.jdacodes.userdemo.core.utils.Constants.AUTH_NAV_GRAPH
import com.jdacodes.userdemo.core.utils.Constants.DASHBOARD_SCREEN
import com.jdacodes.userdemo.core.utils.Constants.FORGOT_PASSWORD_SCREEN
import com.jdacodes.userdemo.core.utils.Constants.HOME_NAV_GRAPH
import com.jdacodes.userdemo.core.utils.Constants.HOME_SCREEN
import com.jdacodes.userdemo.core.utils.Constants.LOGIN_SCREEN
import com.jdacodes.userdemo.core.utils.Constants.PROFILE_SCREEN
import com.jdacodes.userdemo.core.utils.Constants.REGISTER_SCREEN
import com.jdacodes.userdemo.core.utils.Constants.USER_DETAIL_SCREEN
import com.jdacodes.userdemo.core.utils.Constants.USERS_SCREEN

sealed class ScreenRoutes(
    val route: String,
    var arguments: String? = "",
    var title: String? = "",
    var icon: ImageVector? = null
) {
    //Screen Routes
    data object LoginScreen: ScreenRoutes(LOGIN_SCREEN)
    data object RegisterScreen: ScreenRoutes(REGISTER_SCREEN)
    data object ForgotPasswordScreen: ScreenRoutes(FORGOT_PASSWORD_SCREEN)

    data object HomeScreen: ScreenRoutes(HOME_SCREEN)

    data object DashboardScreen : ScreenRoutes(
        route = DASHBOARD_SCREEN,
        title = R.string.home.toString(),
        icon = Icons.Filled.Home,
        arguments = ""
    ) {
        val fullRoute = route + arguments
    }
    data object UsersScreen : ScreenRoutes(
        route = USERS_SCREEN,
        title = R.string.users.toString(),
        icon = Icons.Filled.People,
        arguments = ""
    ) {
        val fullRoute = route + arguments
    }
    data object UserDetailScreen : ScreenRoutes(
        route = USER_DETAIL_SCREEN,
        title = R.string.user_details.toString(),
        icon = null,
        arguments = "/{userId}"
    ) {
        val fullRoute = route + arguments
    }
    data object ProfileScreen : ScreenRoutes(
        route = PROFILE_SCREEN,
        title = R.string.profile.toString(),
        icon = Icons.Filled.Person,
        arguments = ""
    ) {
        val fullRoute = route + arguments
    }

    //Graph Routes
    data object AuthNav : ScreenRoutes(AUTH_NAV_GRAPH)
    data object HomeNav : ScreenRoutes(HOME_NAV_GRAPH)




}