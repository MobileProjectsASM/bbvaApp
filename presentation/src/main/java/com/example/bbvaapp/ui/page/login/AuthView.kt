package com.example.bbvaapp.ui.page.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bbvaapp.R
import com.example.bbvaapp.model.InputState
import com.example.bbvaapp.model.LoginFormUiState
import com.example.bbvaapp.model.LoginUiState
import com.example.bbvaapp.ui.BVVAGeneralTitle
import com.example.bbvaapp.ui.DefaultButton
import com.example.bbvaapp.ui.DefaultImageButton
import com.example.bbvaapp.ui.DefaultOutlinedTextFieldLI
import com.example.bbvaapp.ui.DefaultText
import com.example.bbvaapp.ui.DefaultTextButton
import com.example.bbvaapp.ui.PasswordOutlinedTextField
import com.example.bbvaapp.utils.MessageResolver
import com.example.bbvaapp.vm.LoginVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AuthView(
    loginVM: LoginVM,
    navController: NavHostController,
    messageResolver: MessageResolver,
    snackBarHostState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()

    AuthenticationSection(
        loginVM = loginVM,
        messageResolver = messageResolver
    )
//    LoginState(
//        loginVM = loginVM,
//        navController = navController,
//        messageResolver = messageResolver,
//        snackBarHostState = snackBarHostState
//    )
}

@Composable
fun AuthenticationSection(
    loginVM: LoginVM,
    messageResolver: MessageResolver
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.height(250.dp))
        PanelLogin(
            loginVM = loginVM,
            messageResolver = messageResolver
        )
        Box(modifier = Modifier.height(250.dp))
    }
}

@Composable
fun PanelLogin(
    loginVM: LoginVM,
    messageResolver: MessageResolver
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 10.dp)
        ) {
            BVVAGeneralTitle(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.txt_ttl_login_panel)
            )
            DefaultText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.txt_inf_login_panel),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(50.dp))
            FormLogin(
                loginVM = loginVM,
                messageResolver = messageResolver
            ) {

            }
        }
    }
}

//@Composable
//fun LoginState(
//    loginVM: LoginVM,
//    navController: NavHostController,
//    messageResolver: MessageResolver,
//    snackBarHostState: SnackbarHostState
//) {
//    val loginState: LoginUiState? by loginVM.loginState.collectAsStateWithLifecycle()
//    LaunchedEffect(snackBarHostState.currentSnackbarData) {
//        if (snackBarHostState.currentSnackbarData == null) {
//            loginVM.resetLoginState()
//        }
//    }
//    if (loginUiState == null || loginUiState is LoginUiState.SentOtp) return
//    when (loginUiState) {
//        is LoginUiState.Failure -> {
//            val message = messageResolver.getErrorLogin((loginUiState as LoginUiState.Failure).loginError)
//            LaunchedEffect(true) {
//                snackBarHostState.showSnackbar(message, withDismissAction = true)
//            }
//        }
//        is LoginUiState.Loading -> CircularProgressDialog()
//        is LoginUiState.RegisteredUser -> LaunchedEffect(true) {
//            navController.navigate(MainPage.createRoute((loginUiState as LoginUiState.RegisteredUser).gamerId))
//        }
//        else -> LaunchedEffect(true) {
//            navController.navigate(CreateAccount.createRoute((loginUiState as LoginUiState.UnregisteredUser).userId))
//        }
//    }
//}

@Composable
fun FormLogin(
    loginVM: LoginVM,
    messageResolver: MessageResolver,
    onSubmit: () -> Unit,
) {
    val loginFormState: LoginFormUiState by loginVM.loginFormUiState.collectAsStateWithLifecycle()
    val userIdErrors: List<String> = when (val emailUiState = loginFormState.emailUiState.state) {
        is InputState.Error -> emailUiState.errors.map { messageResolver.getErrorEmail(it) }
        InputState.Init -> listOf()
        InputState.Success -> listOf()
    }
    val passwordErrors: List<String> = when (val passwordUiState = loginFormState.passwordUiState.state) {
        is InputState.Error -> passwordUiState.errors.map { messageResolver.getErrorPassword(it) }
        InputState.Init -> listOf()
        InputState.Success -> listOf()
    }
    Column {
        DefaultOutlinedTextFieldLI(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            value = loginFormState.emailUiState.value,
            label = R.string.txt_label_email_login,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = Icons.Default.Person,
            cdLeadingIcon = R.string.txt_cd_li_email,
            errors = userIdErrors
        ) {
            loginVM.validateLoginForm(it, loginFormState.passwordUiState.value)
        }
        Spacer(modifier = Modifier.height(10.dp))
        PasswordOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            label = R.string.txt_label_password,
            password = loginFormState.passwordUiState.value,
            leadingIcon = Icons.Default.Lock,
            errors = passwordErrors,
        ) {
            loginVM.validateLoginForm(loginFormState.emailUiState.value, it)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DefaultButton(
                text = stringResource(id = R.string.txt_btn_login),
                enable = loginFormState.emailUiState.state is InputState.Success && loginFormState.passwordUiState.state is InputState.Success,
                onClickButton = onSubmit
            )
            Spacer(modifier = Modifier.height(20.dp))
            DefaultTextButton(
                text = stringResource(id = R.string.txt_btn_create_account)
            ) {

            }
        }
    }
}