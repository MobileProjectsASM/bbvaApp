package com.example.bbvaapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bbvaapp.ui.page.login.AuthView
import com.example.bbvaapp.ui.page.login.BackgroundLogin
import com.example.bbvaapp.utils.MessageResolver
import com.example.bbvaapp.vm.LoginVM

@Composable
fun MainNavigation(
    initRoute: String,
    innerPadding: PaddingValues,
    messageResolver: MessageResolver
) {
    val navigationController = rememberNavController()
    NavHost(
        navController = navigationController,
        startDestination = initRoute,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Route.Login.route) { backStackEntry ->
            val loginVM = hiltViewModel<LoginVM>(backStackEntry)
            BackgroundLogin {
                AuthView(
                    loginVM = loginVM,
                    navController = navigationController,
                    messageResolver = messageResolver
                )
            }
        }
        composable(Route.Dashboard.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString(Route.Dashboard.userIdArg) ?: ""
            Box(contentAlignment = Alignment.Center) {
                Text(text = "Dashboard $userId")
            }
        }
    }
}