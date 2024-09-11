package com.jdacodes.userdemo.dashboard.presentation

import com.jdacodes.userdemo.dashboard.domain.model.Color

data class ColorDetailState(
    val isLoading: Boolean = false,
    val color: Color? = null
)
