package com.example.bbvaapp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bbvaapp.utils.MessageResolver

@Composable
fun MainNavigation(
    initRoute: String,
    innerPadding: PaddingValues,
    snackBarHostState: SnackbarHostState,
    messageResolver: MessageResolver
) {
    val navigationController = rememberNavController()
    NavHost(
        navController = navigationController,
        startDestination = initRoute,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Route.Login.route) {

        }
        composable(Route.Dashboard.route) {

        }
    }
}