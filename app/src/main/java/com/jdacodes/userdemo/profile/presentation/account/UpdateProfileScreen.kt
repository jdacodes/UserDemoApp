package com.jdacodes.userdemo.profile.presentation.account

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UpdateProfileScreen(
    viewModel: ProfileViewModel,
    uiState: UpdateProfileUiState,
    userId: Int,
    onClickBack: () -> Unit,
    onUpdateSuccess: (String) -> Unit,
    onUpdateFailure: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    keyboardController: SoftwareKeyboardController
) {
    LaunchedEffect(key1 = true, block = {
        viewModel.getProfileInfo()
    })
//    val profile = viewModel.infoState.value
    val nameState = uiState.form.name
    val jobState = uiState.form.job
    val updatedAtState = uiState.form.updatedAt

    UpdateProfileScreenContent(
        onClickUpdate = {
            viewModel.onFormEvent(UpdateProfileFormEvent.Submit(userId))
            keyboardController.hide()
        },
//        {
//            viewModel.update(userId = userId, profile = profile)
//        },
        onClickBack = onClickBack,
        viewModel = viewModel,
        uiState = uiState,
        nameState = nameState,
        jobState = jobState,
        updatedAtState = updatedAtState,
        onNameTextChange = { viewModel.onFormEvent(UpdateProfileFormEvent.NameChanged(it)) },
        onJobTextChange = { viewModel.onFormEvent(UpdateProfileFormEvent.JobChanged(it)) },
        onUpdatedAtTextChange = { viewModel.onFormEvent(UpdateProfileFormEvent.UpdatedAtChanged(it)) },
        onUpdateSuccess = onUpdateSuccess,
        onUpdateFailure = onUpdateFailure,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        keyboardController = keyboardController,

        )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileScreenContent(
    onClickUpdate: () -> Unit, // when update button is clicked
    onClickBack: () -> Unit, //when topbar back is clicked
    viewModel: ProfileViewModel,
    uiState: UpdateProfileUiState,
    onUpdateSuccess: (String) -> Unit,
    onUpdateFailure: (String) -> Unit,
    nameState: String,
    onNameTextChange: (String) -> Unit,
    jobState: String,
    onJobTextChange: (String) -> Unit,
    updatedAtState: String,
    onUpdatedAtTextChange: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    keyboardController: SoftwareKeyboardController
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Update Profile") },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {

            val outlineTextFieldColors = OutlinedTextFieldDefaults.colors(

                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = MaterialTheme.colorScheme.error,
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.primary
                )

            )
            LazyColumn(contentPadding = paddingValues) {
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Column {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            value = nameState,
                            onValueChange = {
                                onNameTextChange(it)
                            },
                            label = {
                                Text(
                                    text = "Nickname",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ).copy(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
//                                    jobFocusRequester.requestFocus()
                                    focusManager.moveFocus(FocusDirection.Down)
                                }),
                            maxLines = 1,
                            singleLine = true,
                            isError = uiState.form.nameError != null,
                            colors = outlineTextFieldColors
                        )
                        if (uiState.form.nameError != null) {
                            Text(
                                text = uiState.form.nameError ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Column {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
//                                .focusRequester(jobFocusRequester),
                            value = jobState,
                            onValueChange = {
                                onJobTextChange(it)
                            },
                            label = {
                                Text(
                                    text = "Work",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ).copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController.hide()
                                }),
                            maxLines = 1,
                            singleLine = true,
                            isError = uiState.form.jobError != null,
                            colors = outlineTextFieldColors
                        )
                        if (uiState.form.jobError != null) {
                            Text(
                                text = uiState.form.jobError ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onClickUpdate,
                        shape = AbsoluteRoundedCornerShape(40.dp),
                        enabled = !uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        )
                    ) {
                        Text(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            text = "Update Profile",
                            textAlign = TextAlign.Center
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }


    LaunchedEffect(key1 = viewModel.eventUpdateFlow) {
        viewModel.eventUpdateFlow.collectLatest { event ->
            Log.d("UpdateProfileScreenContent", "Event received: $event")
            when (event) {
                is UpdateEvent.UpdateSuccess -> {
                    Log.d("UpdateProfileScreenContent", "Navigating to Profile screen")
                    onUpdateSuccess(event.message)
                }

                is UpdateEvent.UpdateFailure -> {
                    Log.d("UpdateProfileScreenContent", "Update failed: ${event.message}")
                    onUpdateFailure(event.message)
                }

            }
        }
    }
}