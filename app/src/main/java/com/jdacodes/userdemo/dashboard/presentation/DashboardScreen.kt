package com.jdacodes.userdemo.dashboard.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.jdacodes.userdemo.R
import com.jdacodes.userdemo.core.presentation.composables.CircularImage
import com.jdacodes.userdemo.core.utils.UiEvents
import com.jdacodes.userdemo.core.utils.toComposeColor
import com.jdacodes.userdemo.dashboard.domain.model.Color
import com.jdacodes.userdemo.dashboard.presentation.composables.DashboardCarousel
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    uiState: ColorListState,
    snackbarHostState: SnackbarHostState,
    onClickColor: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = true) {
        viewModel.getProfile()
    }
    val user = viewModel.profileState.value

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularImage(
                            imageUrl = user.avatar,
                            size = 36.dp,
                            modifier = Modifier
                                .padding(end = 8.dp),
                            contentDescription = "profile picture"
                        )
                        Text(
                            stringResource(R.string.dashboard_greeting).plus(
                                user.firstName?.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.getDefault()
                                    ) else it.toString()
                                }
                            ),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            Spacer(Modifier.height(16.dp))
            SearchBar(Modifier.padding(horizontal = 16.dp))
            DashboardSection(title = R.string.dashboard_scenery) {
                DashboardCarousel()
            }
            DashboardSection(title = R.string.users_row) {
                UsersRow()
            }
            DashboardSection(title = R.string.colors_collection) {
                ColorsCollectionGrid(
                    viewModel = viewModel,
                    onClickColor = onClickColor,
                    uiState = uiState,
                    snackbarHostState = snackbarHostState,
                    modifier = modifier
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.uiEvents.collectLatest { event ->
            when (event) {
                is UiEvents.SnackBarEvent -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is UiEvents.NavigateEvent -> {
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = {
            Text(stringResource(R.string.placeholder_search))
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    )
}

@Composable
fun DashboardSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
        )
        content()
    }
}

@Composable
fun ColorsCollectionGrid(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel,
    onClickColor: (Int) -> Unit,
    uiState: ColorListState,
    snackbarHostState: SnackbarHostState
) {
    val colors = viewModel.colors.collectAsLazyPagingItems()

    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.height(168.dp)
    ) {
        items(colors.itemCount) { index ->
            val color = colors[index]
            color?.let { it ->
                ColorsCollectionCard(
                    color = it,
                    onClick = { onClickColor(it.id) },
                    Modifier.height(80.dp)
                )
            }
            // Trigger loading the next page when scrolling to the last item
            if (index == colors.itemCount - 1 && colors.loadState.append is LoadState.NotLoading) {
                colors.retry() // This will trigger loading the next page
            }
        }
        colors.apply {
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
                    val e = colors.loadState.refresh as LoadState.Error
                    item {
                        Text("Error: ${e.error.localizedMessage}")
                    }
                }

                loadState.append is LoadState.Error -> {
                    val e = colors.loadState.append as LoadState.Error
                    item {
                        Text("Error: ${e.error.localizedMessage}")
                    }
                }
            }
        }
    }
}

// TODO: Change data passed and image loader composable
@Composable
fun ColorsCollectionCard(
    color: Color,
    onClick: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(255.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_color), // Your image resource
                contentDescription = null,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(color.toComposeColor()), // Use the converted color
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = color.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
fun UsersRow(
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier
    ) {
        // TODO: Change data source
//        items(alignYourBodyData) { item ->
//            UsersElement(item.drawable, item.text)
//    }
    }
}

// TODO: Change data passed and image loader composable
@Composable
fun UsersElement(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
        )
        Text(
            text = stringResource(text),
            modifier = Modifier.paddingFromBaseline(top = 24.dp, bottom = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}