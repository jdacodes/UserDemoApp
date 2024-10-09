package com.jdacodes.userdemo.profile.presentation.account

sealed class DeleteEvent {
    data class DeleteSuccess(val message: String) : DeleteEvent()
    data class DeleteFailure(val message: String) : DeleteEvent()
}