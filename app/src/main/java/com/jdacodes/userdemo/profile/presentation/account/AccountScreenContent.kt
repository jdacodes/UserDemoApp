package com.jdacodes.userdemo.profile.presentation.account

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jdacodes.userdemo.core.presentation.composables.CircularImage
import com.jdacodes.userdemo.core.utils.toFormattedDate
import com.jdacodes.userdemo.profile.domain.model.Profile
import com.jdacodes.userdemo.profile.domain.model.User
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    viewModel: ProfileViewModel,
    user: User,
    onClickUpdateProfile: () -> Unit,
    onClickLogout: () -> Unit,
    onLogoutSuccess: (String) -> Unit,
    onLogoutFailure: (String) -> Unit,
    onClickDelete: () -> Unit,
    onDeleteSuccess: (String) -> Unit,
    onDeleteFailure: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    profile: UpdateProfileUiState
) {

    val openDialog = remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                icon = { Icon(Icons.Filled.DeleteForever, contentDescription = null) },
                title = { Text(text = "Delete Account") },
                text = { Text(text = "You are about to delete your account") },
                confirmButton = {
                    TextButton(onClick = {
                        openDialog.value = false
                        onClickDelete()
                    }) { Text("Confirm") }
                },
                dismissButton = {
                    TextButton(onClick = { openDialog.value = false }) { Text("Dismiss") }
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn {
                item {
                    UserProfile(
                        user = user,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        onClickUpdateProfile = onClickUpdateProfile

                    )
                }

                item {
                    ProfileInfo(profile = profile)
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.padding(8.dp),
                        onClick = onClickLogout,
                        shape = RoundedCornerShape(40.dp),
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.primary,
                        )
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            text = "Log Out",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                item {
                    TextButton(
                        modifier = Modifier.padding(8.dp),
                        onClick = {openDialog.value = true},
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            text = "Delete my account",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

    }
    LaunchedEffect(key1 = viewModel.eventFlow) {
        viewModel.eventFlow.collectLatest { event ->
            Log.d("ProfileScreenContent", "Event received: $event")
            when (event) {
                is LogoutEvent.LogoutSuccess -> {
                    Log.d("ProfileScreenContent", "Navigating to Login")
                    onLogoutSuccess(event.message)
                }

                is LogoutEvent.LogoutFailure -> {
                    Log.d("ProfileScreenContent", "Logout failed: ${event.message}")
                    onLogoutFailure(event.message)
                }

            }
        }
    }
    LaunchedEffect(key1 = viewModel.eventDeleteChannel) {
        viewModel.eventDeleteChannel.collectLatest { event ->
            Log.d("ProfileScreenContent", "Event received: $event")
            when (event) {
                is DeleteEvent.DeleteSuccess -> {
                    Log.d("ProfileScreenContent", "Navigating to Login")
                    onDeleteSuccess(event.message)
                }

                is DeleteEvent.DeleteFailure -> {
                    Log.d("ProfileScreenContent", "Delete failed: ${event.message}")
                    onDeleteFailure(event.message)
                }

            }
        }
    }
}

@Composable
fun ProfileInfo(
    profile: UpdateProfileUiState
) {
    Card(
        modifier = Modifier.padding(8.dp),
        border = BorderStroke(0.3.dp, MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Nick name: ",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    ),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = profile.form.name ?: "",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                    ),
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Work: ",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    ),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = profile.form.job,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                    ),
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Last updated: ",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    ),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = profile.form.updatedAt.trim().toFormattedDate("dd MMM yyyy, HH:mm a"),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                    ),
                )
            }
        }
    }
}

@Composable
fun UserProfile(
    user: User,
    modifier: Modifier,
    onClickUpdateProfile: () -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
        ),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface

        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            CircularImage(
                imageUrl = user.avatar,
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)
                    .fillMaxHeight()
            )
            Spacer(modifier = Modifier.width(5.dp))
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(5.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "${
                        user.firstName?.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        }
                    }  ${
                        user.lastName?.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        }
                    }",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,

                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "@${user.email}",
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    modifier = Modifier.align(Alignment.End),
                    onClick = onClickUpdateProfile,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    shape = RoundedCornerShape(40.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(3.dp),
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        text = "Edit profile",
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }
    }
}
