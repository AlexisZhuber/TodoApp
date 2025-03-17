package com.alexismoraportal.todoapp.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexismoraportal.todoapp.ui.screens.NotificationScreen
import com.alexismoraportal.todoapp.ui.screens.TasksScreen

/**
 * Duration (in milliseconds) for all transition animations.
 */
private const val ANIMATION_DURATION = 800

@Composable
fun NavManager(){
    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = Screens.HomeScreen.name){
        composable (
            route = Screens.HomeScreen.name,
            enterTransition = {
                // The screen slides into view from the bottom.
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            },
            exitTransition = {
                // The screen slides out of view downward.
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            }
        ){
            TasksScreen(navController)
        }

        composable (
            route = Screens.NotificationScreen.name,
            enterTransition = {
                // The screen slides into view from the bottom.
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            },
            exitTransition = {
                // The screen slides out of view downward.
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            }
        ){
            NotificationScreen(navController)
        }
    }
}