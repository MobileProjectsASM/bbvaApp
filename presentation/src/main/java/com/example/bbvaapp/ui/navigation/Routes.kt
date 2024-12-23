package com.example.bbvaapp.ui.navigation

sealed class Route(val route: String) {
    data object Login: Route("login")
    data object Dashboard: Route("dashboard/{userId}") {
        const val userIdArg = "userId"

        fun createRoute(userId: String) = "dashboard/$userId"
    }
}