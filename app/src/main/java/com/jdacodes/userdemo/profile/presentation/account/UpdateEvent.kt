package com.jdacodes.userdemo.profile.presentation.account

sealed class UpdateEvent {
    data class UpdateSuccess(val message: String) : UpdateEvent()
    data class UpdateFailure(val message: String) : UpdateEvent()
}