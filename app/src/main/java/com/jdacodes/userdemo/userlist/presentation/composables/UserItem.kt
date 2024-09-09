package com.jdacodes.userdemo.userlist.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jdacodes.userdemo.core.presentation.composables.CircularImage
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
            .padding(horizontal = 8.dp, vertical = 4.dp),
//        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            CircularImage(
                imageUrl = user.avatar
            )
            Column() {
                Row() {
                    Text(
                        text = "${user.firstName} " ?: "",
                        //                style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = user.lastName ?: "",
                        //                style = MaterialTheme.typography.h6
                    )
                }
                Text(
                    text = user.email ?: "",
                    //                style = MaterialTheme.typography.h6
                )
            }
        }
    }
}
