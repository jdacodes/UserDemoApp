package com.jdacodes.userdemo

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.jdacodes.userdemo.auth.presentation.splash.SplashViewModel
import com.jdacodes.userdemo.core.presentation.theme.UserDemoTheme
import com.jdacodes.userdemo.navigation.RootNav
import com.jdacodes.userdemo.navigation.ScreenRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen().apply {
            setOnExitAnimationListener { viewProvider ->
                ObjectAnimator.ofFloat(
                    viewProvider.view,
                    "scaleX",
                    0.5f, 0f
                ).apply {
                    interpolator = OvershootInterpolator()
                    duration = 300
                    doOnEnd { viewProvider.remove() }
                    start()
                }
                ObjectAnimator.ofFloat(
                    viewProvider.view,
                    "scaleY",
                    0.5f, 0f
                ).apply {
                    interpolator = OvershootInterpolator()
                    duration = 300
                    doOnEnd { viewProvider.remove() }
                    start()
                }
            }
        }
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            splashViewModel.isLoading.value
        }


        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        setContent {
            UserDemoTheme {
                val navController = rememberNavController()
                val keyboardController = LocalSoftwareKeyboardController.current
                val snackbarHostState = remember { SnackbarHostState() }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    //SplashViewModel states
                    val isLoading by splashViewModel.isLoading.collectAsStateWithLifecycle(
                        initialValue = true
                    )
                    val isLoggedIn by splashViewModel.eventState.collectAsStateWithLifecycle(
                        initialValue = false
                    )
                    //Navigate to User list or Login Screen depending on if the user is logged-in or not
                    if (isLoading) {
                        // Show a loading indicator
                        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                    } else {
                        // Navigate based on login state
                        val startDestination =
//                            if (isLoggedIn) ScreenRoutes.UsersScreen.route else ScreenRoutes.LoginScreen.route
                            if (isLoggedIn) ScreenRoutes.HomeNav.route else ScreenRoutes.AuthNav.route

                        LaunchedEffect(Unit) {
                            navController.navigate(startDestination) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        }

                        RootNav(
                            navController = navController,
                            snackbarHostState = snackbarHostState,
                            keyboardController = keyboardController!!,
                            startDestination = startDestination
                        )
                    }
                }
            }
        }
    }
}