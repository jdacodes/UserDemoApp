package com.jdacodes.userdemo.dashboard.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jdacodes.userdemo.core.presentation.theme.getTextColor
import com.jdacodes.userdemo.dashboard.domain.model.Color

@Composable
fun ColorDetailElement(
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = color.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = getTextColor()
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Pantone: ${color.pantoneValue}",
            color = getTextColor(),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Light,
                fontSize = 16.sp,
                color = getTextColor()
                ),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Year: ${color.year}",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Light,
                fontSize = 16.sp,
                color = getTextColor()
            ),
        )

    }
}