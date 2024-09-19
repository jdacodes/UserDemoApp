package com.jdacodes.userdemo.userlist.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jdacodes.userdemo.core.presentation.composables.CircularImage
import com.jdacodes.userdemo.core.presentation.theme.getGradientBackground
import com.jdacodes.userdemo.core.presentation.theme.getTextColor
import com.jdacodes.userdemo.userlist.domain.model.User

@Composable
fun UserItem(
    user: User,
    onClick: (User) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(user) }
            .padding(horizontal = 8.dp, vertical = 4.dp)


//        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .background(brush = getGradientBackground())
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            CircularImage(
                imageUrl = user.avatar
            )
            Column() {
                Row() {
                    Text(
                        text = "${user.firstName} " ?: "",
                        color = getTextColor(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                        )
                    )
                    Text(
                        text = user.lastName ?: "",
                        color = getTextColor(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                        )
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = user.email ?: "",
                    color = getTextColor().copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                    )
                )
            }
        }
    }
}
