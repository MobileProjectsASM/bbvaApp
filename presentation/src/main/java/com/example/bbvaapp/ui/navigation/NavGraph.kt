package com.example.bbvaapp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bbvaapp.ui.page.login.AuthView
import com.example.bbvaapp.ui.page.BackgroundPage
import com.example.bbvaapp.ui.page.dashboard.DashboardView
import com.example.bbvaapp.utils.MessageResolver
import com.example.bbvaapp.vm.LoginVM
import com.example.bbvaapp.vm.SessionVM

@Composable
fun MainNavigation(
    initRoute: String,
    innerPadding: PaddingValues,
    messageResolver: MessageResolver,
    sessionVM: SessionVM
) {
    val navigationController = rememberNavController()
    NavHost(
        navController = navigationController,
        startDestination = initRoute,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Route.Login.route) { backStackEntry ->
            val loginVM = hiltViewModel<LoginVM>(backStackEntry)
            BackgroundPage {
                AuthView(
                    loginVM = loginVM,
                    navController = navigationController,
                    messageResolver = messageResolver
                )
            }
        }
        composable(Route.Dashboard.route) {
            LaunchedEffect(true) {
                sessionVM.fetchSessionInfo()
            }
            BackgroundPage {
                DashboardView()
            }
        }
    }
}