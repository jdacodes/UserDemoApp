package com.jdacodes.userdemo.profile.presentation.account

data class UpdateProfileUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val form: UpdateProfileForm = UpdateProfileForm()
)

data class UpdateProfileForm(
    val name: String = "",
    val nameError: String? = null,
    val job: String = "",
    val jobError: String? = null,
    val updatedAt: String = "",
    val updatedAtError: String? = null,
)