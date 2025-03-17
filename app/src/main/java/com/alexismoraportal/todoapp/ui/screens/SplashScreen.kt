package com.alexismoraportal.todoapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.alexismoraportal.todoapp.R

/**
 * A composable that displays a splash screen using a Lottie animation.
 *
 * @param onAnimationFinished Callback that gets executed once the animation finishes.
 */
@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    // Load the Lottie composition from the raw resources (e.g., res/raw/tablet.json)
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.tablet)
    )

    // Animate the Lottie composition. The animation will run only once (iterations = 1)
    val animationState = animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    // Observe the animation progress. When the progress nears 1.0 (completion),
    // call the onAnimationFinished callback.
    LaunchedEffect(animationState) {
        snapshotFlow { animationState.progress }
            .collect { progress ->
                // Use a threshold of 0.99 to account for any floating-point precision issues.
                if (progress >= 0.99f) {
                    onAnimationFinished()
                }
            }
    }

    // Display the Lottie animation centered in the full available screen space.
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { animationState.progress }
        )
    }
}
