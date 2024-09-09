package com.jdacodes.userdemo.auth.presentation.login

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collectLatest

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginScreenContent(
    modifier: Modifier,
    viewModel: LoginViewModel,
    uiState: LoginUiState,
    snackbarHostState: SnackbarHostState,
    onClickLogin: () -> Unit,
    onLoginSuccess: (String) -> Unit,
    onLoginFailure: (String) -> Unit,
    emailState: String,
    passwordState: String,
    rememberMeState: Boolean,
    onEmailTextChange: (String) -> Unit,
    onPasswordTextChange: (String) -> Unit,
    onRememberMeClicked: (Boolean) -> Unit,
    onClickForgotPassword: () -> Unit,
    onClickDontHaveAccount: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("User Login") },
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
                            modifier = Modifier.fillMaxWidth(),
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
                            ),
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
                            modifier = Modifier.fillMaxWidth(),
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
                            ),
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
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = rememberMeState, onCheckedChange = {
                                onRememberMeClicked(it)
                            })
                            Text(
                                text = "Remember me",
                                fontSize = 12.sp,
//                                fontFamily = fontFamily,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        TextButton(onClick = onClickForgotPassword) {
                            Text(
                                text = "Forgot password?",
                                color = MaterialTheme.colorScheme.tertiary,
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onClickLogin,
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
                                .padding(12.dp), text = "Sign In", textAlign = TextAlign.Center
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(
                        onClick = onClickDontHaveAccount,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("Don't have an account?")
                                append(" ")
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                ) {
                                    append("Sign Up")
                                }
                            },
//                            fontFamily = fontFamily,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface,
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
            Log.d("LoginScreenContent", "Event received: $event")
            when (event) {
                is LoginEvent.LoginSuccess -> {
                    Log.d("LoginScreenContent", "Navigating to UserList")
                    onLoginSuccess(event.message)
                }

                is LoginEvent.LoginFailure -> {
                    Log.d("LoginScreenContent", "Login failed: ${event.message}")
                    onLoginFailure(event.message)
                }

                is LoginEvent.ValidationError -> {
                    Log.d("LoginScreenContent", "Validation error received: ${event.message}")
                    event.message?.let { message ->
                        Log.d("LoginScreenContent", "Showing snackbar with message: $message")
                        snackbarHostState.showSnackbar(message)
                    }
                }
            }
        }
    }
}