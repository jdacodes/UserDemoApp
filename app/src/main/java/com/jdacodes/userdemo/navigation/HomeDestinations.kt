package com.jdacodes.userdemo.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.jdacodes.userdemo.R

enum class HomeDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
) {
    DASHBOARD(R.string.home, Icons.Default.Home, R.string.home),
    USERS(R.string.users, Icons.Default.People, R.string.users),
    PROFILE(R.string.profile, Icons.Default.Person, R.string.profile),

}