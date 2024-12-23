package com.example.bbvaapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.bbvaapp.model.SessionUiState
import com.example.bbvaapp.ui.navigation.MainNavigation
import com.example.bbvaapp.ui.navigation.Route
import com.example.bbvaapp.ui.theme.BbvaAppTheme
import com.example.bbvaapp.utils.MessageResolver
import com.example.bbvaapp.vm.SessionViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val sessionViewModel: SessionViewModel by viewModels()

    @Inject
    lateinit var messageResolver: MessageResolver

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        var keepSplashScreen = true
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }
        launchOverStarted(sessionViewModel.sessionState::collect) { sessionState ->
            when (sessionState) {
                SessionUiState.Fail -> {
                    keepSplashScreen = true
                    Snackbar.make(window.decorView, "", Snackbar.LENGTH_SHORT).show()
                }
                SessionUiState.Loading -> keepSplashScreen = true
                is SessionUiState.Success -> {
                    keepSplashScreen = false
                    setContent {
                        BbvaAppTheme {
                            BBVAScaffold(
                                initRoute = if (sessionState.isActive) Route.Dashboard.route else Route.Login.route,
                                messageResolver = messageResolver
                            )
                        }
                    }
                }
            }
        }
        sessionViewModel.verifySessionActive()
        enableEdgeToEdge()
    }
}

@Composable
fun BBVAScaffold(
    initRoute: String,
    messageResolver: MessageResolver
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        MainNavigation(
            initRoute,
            innerPadding,
            snackBarHostState,
            messageResolver
        )
    }
}

fun <T> MainActivity.launchOverStarted(collect: suspend (FlowCollector<T>) -> Nothing, collector: FlowCollector<T>) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            collect(collector)
        }
    }
}