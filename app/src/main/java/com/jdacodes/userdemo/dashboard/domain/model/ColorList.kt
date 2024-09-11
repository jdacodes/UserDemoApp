package com.jdacodes.userdemo.dashboard.domain.model

data class ColorList(
    val data: List<Color>,
    val page: Int,
    val perPage: Int,
    val support: Support,
    val total: Int,
    val totalPages: Int
)
