package com.alexismoraportal.todoapp.navigation

sealed class Screens(val name: String) {
    data object HomeScreen: Screens("Home")
    data object NotificationScreen: Screens("Notification")
}