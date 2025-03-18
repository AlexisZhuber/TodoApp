package com.alexismoraportal.todoapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alexismoraportal.todoapp.ui.theme.Secondary

/**
 * BottomBarMenu displays a bottom bar with a centered FloatingActionButton (FAB) and
 * icon buttons for search and notifications. The notifications icon displays a badge
 * showing the number of tasks due within the next hour.
 *
 * @param onAddClick Callback invoked when the FAB is clicked.
 * @param onSearchClick Callback invoked when the search icon is clicked.
 * @param onNotificationsClick Callback invoked when the notifications icon is clicked.
 * @param notificationBadgeCount The number of tasks due soon to be displayed in the badge.
 */
@Composable
fun BottomBarMenu(
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    notificationBadgeCount: Int = 0
) {
    // Overall height for the bottom bar.
    val barHeight = 64.dp
    // Radius value to offset the FAB upwards.
    val radius = 32.dp
    var isSearchActive by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(barHeight)
            .background(
                color = Secondary,
                shape = roundedCenterShape(
                    circleDiameter = 64.dp,   // Diameter of the central notch.
                    topCornerRadius = 48.dp,    // Radius for the top corners of the bar.
                    depthFactor = 1f            // Depth factor for the central notch curve.
                )
            )
    ) {
        // Row for search and notifications icons.
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End,
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
            // Notifications icon with badge.
            NotificationsIcon(
                badgeCount = notificationBadgeCount,
                onNotificationsClick = onNotificationsClick
            )
        }
        // FloatingActionButton placed in the center top, offset upward.
        FloatingActionButton(
            onClick = onAddClick,
            containerColor = Secondary,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = -radius)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }
    }
}
