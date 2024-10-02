package com.jdacodes.userdemo.profile.presentation.account

sealed class UpdateProfileFormEvent {
    data class NameChanged(val name: String) : UpdateProfileFormEvent()
    data class JobChanged(val job: String) : UpdateProfileFormEvent()
    data class UpdatedAtChanged(val updatedAt: String) : UpdateProfileFormEvent()
    data class Submit(val userId: Int): UpdateProfileFormEvent()
}