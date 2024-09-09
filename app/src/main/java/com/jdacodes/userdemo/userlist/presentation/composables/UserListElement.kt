package com.jdacodes.userdemo.userlist.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.jdacodes.userdemo.userlist.presentation.UserListState
import com.jdacodes.userdemo.userlist.presentation.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListElement(
    viewModel: UserViewModel,
    onClickUser: (Int) -> Unit,
    uiState: UserListState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val users = viewModel.users.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Users") },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(users.itemCount) { index ->
                    val user = users[index]
                    user?.let { it ->
                        UserItem(user = it, onClick = { onClickUser(it.id) })
                    }
                    // Trigger loading the next page when scrolling to the last item
                    if (index == users.itemCount - 1 && users.loadState.append is LoadState.NotLoading) {
                        users.retry() // This will trigger loading the next page
                    }
                }

                users.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        loadState.append is LoadState.Loading -> {

                            item {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val e = users.loadState.refresh as LoadState.Error
                            item {
                                Text("Error: ${e.error.localizedMessage}")
                            }
                        }

                        loadState.append is LoadState.Error -> {
                            val e = users.loadState.append as LoadState.Error
                            item {
                                Text("Error: ${e.error.localizedMessage}")
                            }
                        }
                    }
                }
            }
        }
    }
}


