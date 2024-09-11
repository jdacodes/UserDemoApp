package com.jdacodes.userdemo.dashboard.presentation.composables


import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.jdacodes.userdemo.R
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardCarousel(
) {
    val carouselState = rememberCarouselState { 3 }
    Box(modifier = Modifier.height(200.dp), contentAlignment = Alignment.Center) {
        HorizontalMultiBrowseCarousel(
            state = carouselState,
            preferredItemWidth = 300.dp,
            itemSpacing = 10.dp,
        ) { page ->
            Box(modifier = Modifier.size(300.dp)) {
                val id = when (page) {
                    0 -> R.drawable.img_carousel1
                    1 -> R.drawable.img_carousel2
                    else -> R.drawable.img_carousel3
                }
                CarouselImage(
                    id = id,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun CarouselImage(
    @DrawableRes id: Int,
    contentScale: ContentScale = ContentScale.Crop,
    crossFade: Boolean = true,
    contentDescription: String? = null,
    @DrawableRes placeholder: Int = R.drawable.ic_avatar_placeholder,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val listener = object : ImageRequest.Listener {
        override fun onError(request: ImageRequest, result: ErrorResult) {
            super.onError(request, result)
        }

        override fun onSuccess(request: ImageRequest, result: SuccessResult) {
            super.onSuccess(request, result)
        }
    }
    val imageRequest = ImageRequest.Builder(context)
        .data(id)
        .listener(listener)
        .dispatcher(Dispatchers.IO)
        .memoryCacheKey(id.toString())
        .diskCacheKey(id.toString())
        .placeholder(placeholder)
        .crossfade(crossFade)
        .error(placeholder)
        .fallback(placeholder)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
    // Load and display the image with AsyncImage
    AsyncImage(
        model = imageRequest,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
    )
}