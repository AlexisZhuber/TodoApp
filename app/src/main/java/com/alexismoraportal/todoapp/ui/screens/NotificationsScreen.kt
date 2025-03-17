package com.alexismoraportal.todoapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alexismoraportal.todoapp.navigation.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NotificationScreen(navController: NavController){
    val scope = rememberCoroutineScope()
    var isClicked by remember { mutableStateOf(false) }
    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // Header / Title.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = "Back",
                        modifier = Modifier
                            .clickable(enabled = !isClicked) {
                                isClicked = true
                                navController.navigate(Screens.HomeScreen.name) {
                                    popUpTo(0) { inclusive = true }
                                }
                                // Reset isClicked after 500 milliseconds
                                scope.launch {
                                    delay(500)
                                    isClicked = false
                                }
                            }
                            .padding(top = 2.dp)
                    )
                    Spacer(Modifier.width(32.dp))
                    Text(
                        text = "TODO APP",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )

                }
            }

        }
    }
}