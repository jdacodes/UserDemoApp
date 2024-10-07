package com.jdacodes.userdemo.auth.presentation.register

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
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jdacodes.userdemo.R
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreenContent(
    modifier: Modifier,
    viewModel: RegisterViewModel,
    uiState: RegisterUiState,
    snackbarHostState: SnackbarHostState,
    onClickRegister: () -> Unit,
    onRegisterSuccess: (String) -> Unit,
    onRegisterFailure: (String) -> Unit,
    emailState: String,
    passwordState: String,
    confirmState: String,
    onEmailTextChange: (String) -> Unit,
    onPasswordTextChange: (String) -> Unit,
    onConfirmTextChange: (String) -> Unit,
    onBackClick: () -> Unit,
    keyboardController: SoftwareKeyboardController,
) {
    val emailFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("User Registration") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
            contentAlignment = Alignment.Center
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
                                .focusRequester(emailFocusRequester),
                            value = emailState,
                            onValueChange = {
                                onEmailTextChange(it)
                            },
                            label = {
                                Text(
                                    text = "Email",
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
                                    focusManager.moveFocus(FocusDirection.Down)
                                }),
                            maxLines = 1,
                            singleLine = true,
                            isError = uiState.form.emailError != null,
                            colors = outlineTextFieldColors
                        )
                        if (uiState.form.emailError != null) {
                            Text(
                                text = uiState.form.emailError ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                item {
                    var passwordVisible by rememberSaveable { mutableStateOf(false) }
                    Spacer(modifier = Modifier.height(16.dp))

                    Column {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = passwordState,
                            onValueChange = {
                                onPasswordTextChange(it)
                            },
                            label = {
                                Text(
                                    text = "Password",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                            ).copy(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }),
                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Outlined.Visibility
                                else Icons.Outlined.VisibilityOff

                                // Please provide localized description for accessibility services
                                val description =
                                    if (passwordVisible) "Hide password" else "Show password"

                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(imageVector = image, description)
                                }
                            },
                            maxLines = 1,
                            singleLine = true,
                            isError = uiState.form.passwordError != null,
                            colors = outlineTextFieldColors
                        )
                        if (uiState.form.passwordError != null) {
                            Text(
                                text = uiState.form.passwordError ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                item {
                    var confirmVisible by rememberSaveable { mutableStateOf(false) }
                    Spacer(modifier = Modifier.height(16.dp))

                    Column {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = confirmState,
                            onValueChange = {
                                onConfirmTextChange(it)
                            },
                            label = {
                                Text(
                                    text = stringResource(R.string.register_confirm_password),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            visualTransformation = if (confirmVisible) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                            ).copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    keyboardController.hide()
                                }),
                            trailingIcon = {
                                val image = if (confirmVisible)
                                    Icons.Outlined.Visibility
                                else Icons.Outlined.VisibilityOff

                                // Please provide localized description for accessibility services
                                val description =
                                    if (confirmVisible) "Hide password" else "Show password"

                                IconButton(onClick = { confirmVisible = !confirmVisible }) {
                                    Icon(imageVector = image, description)
                                }
                            },
                            maxLines = 1,
                            singleLine = true,
                            isError = uiState.form.confirmError != null,
                            colors = outlineTextFieldColors
                        )
                        if (uiState.form.confirmError != null) {
                            Text(
                                text = uiState.form.confirmError ?: "",
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
                        onClick = onClickRegister,
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
                                .padding(12.dp), text = "Sign Up", textAlign = TextAlign.Center
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

    LaunchedEffect(key1 = viewModel.eventFlow) {
        viewModel.eventFlow.collectLatest { event ->
            Log.d("RegisterScreenContent", "Event received: $event")
            when (event) {
                is RegisterEvent.RegisterSuccess -> {
                    Log.d("RegisterScreenContent", "Navigating to UserList")
                    onRegisterSuccess(event.message)
                }

                is RegisterEvent.RegisterFailure -> {
                    Log.d("RegisterScreenContent", "Registration failed: ${event.message}")
                    onRegisterFailure(event.message)
                }

                is RegisterEvent.ValidationError -> {
                    Log.d("RegisterScreenContent", "Validation error received: ${event.message}")
                    event.message?.let { message ->
                        Log.d("RegisterScreenContent", "Showing snackbar with message: $message")
                        snackbarHostState.showSnackbar(message)
                    }
                }
            }
        }
    }
}