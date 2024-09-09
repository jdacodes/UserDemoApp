package com.jdacodes.userdemo.userlist.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jdacodes.userdemo.core.presentation.composables.CircularImage
import com.jdacodes.userdemo.core.utils.UiEvents
import com.jdacodes.userdemo.userlist.presentation.UserDetailState
import com.jdacodes.userdemo.userlist.presentation.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    viewModel: UserViewModel,
    uiState: UserDetailState,
    userId: Int,
    onClickBack:() -> Unit
) {


    // Call getUserById when the screen is first composed
    LaunchedEffect(userId) {
        viewModel.getUserById(userId)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvents.SnackBarEvent -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is UiEvents.NavigateEvent -> {
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Details") },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                uiState.user?.let { user ->
                    user.avatar?.let {
                        CircularImage(
                            imageUrl = it,
                            size = 100.dp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row() {
                        Text(
                            text = "${user.firstName} " ?: "",
                            //                style = MaterialTheme.typography.h6
                        )
                        Text(
                            text = user.lastName ?: "",
                            //                style = MaterialTheme.typography.h6
                        )
                    }
                    Text(text = "${user.email}" ?: "")
                    // Add more user details as needed
                }

            }
        }
    }
}
