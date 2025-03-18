package com.alexismoraportal.todoapp.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import com.alexismoraportal.todoapp.ui.theme.Primary
import kotlinx.coroutines.launch

/**
 * NotificationsIcon displays the notifications icon with an optional badge showing
 * the count of tasks due within the next hour.
 *
 * @param badgeCount The number of tasks due soon.
 * @param onNotificationsClick Callback invoked when the icon is clicked.
 */
@Composable
fun NotificationsIcon(
    badgeCount: Int = 0,
    onNotificationsClick: () -> Unit
) {
    // Create an Animatable for the rotation value (starting at 0 degrees)
    val rotationAnim = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    var iconColor by remember { mutableStateOf(Color.White) }

    Box {
        IconButton(
            onClick = {
                onNotificationsClick()
                iconColor = Primary
                // Launch the notification icon shake animation
                coroutineScope.launch {
                    rotationAnim.animateTo(targetValue = 15f, animationSpec = tween(durationMillis = 50))
                    rotationAnim.animateTo(targetValue = -15f, animationSpec = tween(durationMillis = 100))
                    rotationAnim.animateTo(targetValue = 10f, animationSpec = tween(durationMillis = 100))
                    rotationAnim.animateTo(targetValue = -10f, animationSpec = tween(durationMillis = 100))
                    rotationAnim.animateTo(targetValue = 0f, animationSpec = tween(durationMillis = 50))
                    iconColor = Color.White
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = iconColor,
                modifier = Modifier.graphicsLayer(rotationZ = rotationAnim.value)
            )
        }
        if (badgeCount > 0) {
            // Display a badge with the count of tasks due soon.
            Badge(
                modifier = Modifier.offset(x = 4.dp, y = (-4).dp),
                containerColor = Color.Red
            ) {
                Text(
                    text = badgeCount.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
