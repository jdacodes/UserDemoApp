package com.jdacodes.userdemo.dashboard.presentation

import com.jdacodes.userdemo.dashboard.domain.model.Color

data class ColorListState(
    val colorlist: List<Color> = emptyList(),
    val isLoading: Boolean = false
)
