package com.alexismoraportal.todoapp.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.launch

@Composable
fun NotificationsIcon(onNotificationsClick: () -> Unit) {
    // Create an Animatable for the rotation value (starting at 0 degrees)
    val rotationAnim = remember { Animatable(0f) }
    // Create a coroutine scope for launching the animation
    val coroutineScope = rememberCoroutineScope()

    IconButton(
        onClick = {
            // Trigger the default notification click behavior
            onNotificationsClick()
            // Launch the bell shake animation
            coroutineScope.launch {
                // Animate to 15 degrees over 50ms (rotate right)
                rotationAnim.animateTo(
                    targetValue = 15f,
                    animationSpec = tween(durationMillis = 50)
                )
                // Animate to -15 degrees over 100ms (rotate left)
                rotationAnim.animateTo(
                    targetValue = -15f,
                    animationSpec = tween(durationMillis = 100)
                )
                // Animate to 10 degrees over 100ms (rotate right)
                rotationAnim.animateTo(
                    targetValue = 10f,
                    animationSpec = tween(durationMillis = 100)
                )
                // Animate to -10 degrees over 100ms (rotate left)
                rotationAnim.animateTo(
                    targetValue = -10f,
                    animationSpec = tween(durationMillis = 100)
                )
                // Animate back to 0 degrees over 50ms (return to original position)
                rotationAnim.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 50)
                )
            }
        }
    ) {
        // Apply the animated rotation to the Icon using graphicsLayer modifier.
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            tint = Color.White,
            modifier = Modifier.graphicsLayer(
                rotationZ = rotationAnim.value
            )
        )
    }
}
