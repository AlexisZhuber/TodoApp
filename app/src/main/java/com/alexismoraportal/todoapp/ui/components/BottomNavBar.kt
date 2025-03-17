package com.alexismoraportal.todoapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alexismoraportal.todoapp.ui.theme.Secondary

/**
 * BottomBarMenu
 *
 * A custom composable that displays a bottom bar with a centered notch at the top.
 * Inside that notch, a FloatingActionButton (FAB) is placed so that it visually
 * overlaps the top edge, giving a "cutout" appearance.
 *
 * Additionally, when the search icon is pressed, it toggles between the search icon
 * and the clear icon.
 *
 * @param onAddClick           Callback invoked when the FAB is clicked (the "+" icon).
 * @param onSearchClick        Callback invoked when the search icon is pressed.
 * @param onNotificationsClick Callback invoked when the notifications icon is pressed.
 */
@Composable
fun BottomBarMenu(
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    // Overall height for the bottom bar.
    val barHeight = 64.dp

    // Radius value to offset the FAB upwards (half of its diameter).
    val radius = 32.dp

    // Local state to toggle the search icon between Search and Clear.
    var isSearchActive by remember { mutableStateOf(false) }

    // Outer container that holds the custom-shaped bottom bar.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            // Provide enough height so the FAB can be placed partially above and still be clickable.
            .height(barHeight)
            .background(
                color = Secondary,
                shape = roundedCenterShape(
                    circleDiameter = 64.dp,   // Diameter of the central notch.
                    topCornerRadius = 48.dp,    // Radius for the top corners of the bar.
                    depthFactor = 1f           // Depth factor for the central notch curve.
                )
            )
    ) {
        // Row containing icon buttons (Search/Clear and Notifications) aligned to the right.
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),  // Horizontal padding for spacing.
            horizontalArrangement = Arrangement.End,    // Align icons to the end (right side).
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon button for search/clear action.
            IconButton(
                onClick = {
                    isSearchActive = !isSearchActive
                    onSearchClick()
                }
            ) {
                Icon(
                    imageVector = if (isSearchActive) Icons.Default.Clear else Icons.Default.Search,
                    contentDescription = if (isSearchActive) "Clear" else "Search",
                    tint = Color.White
                )
            }

            NotificationsIcon(onNotificationsClick = onNotificationsClick)
        }

        // The FloatingActionButton placed in the center top, offset upward.
        FloatingActionButton(
            onClick = onAddClick,
            containerColor = Secondary,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = -radius)  // Offset by 32.dp upward.
        ) {
            // "+" icon inside the FAB.
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }
    }
}
