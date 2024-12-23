package com.example.bbvaapp.ui.page.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.bbvaapp.R
import com.example.bbvaapp.model.SessionInfoError
import com.example.bbvaapp.model.SessionInfoUiState
import com.example.bbvaapp.ui.CircularProgressDialog
import com.example.bbvaapp.ui.DefaultText
import com.example.bbvaapp.ui.MessageDialog
import com.example.bbvaapp.utils.MessageResolver
import com.example.bbvaapp.vm.SessionVM

@Composable
fun DashboardView(
    sessionVM: SessionVM,
    navController: NavHostController,
    messageResolver: MessageResolver
) {
    val sessionInfoState: SessionInfoUiState? by sessionVM.sessionInfoState.collectAsStateWithLifecycle()
    if (sessionInfoState == null) return
    val state = sessionInfoState as SessionInfoUiState
    when (state) {
        is SessionInfoUiState.ErrorToLoadImage -> TODO()
        is SessionInfoUiState.Fail -> when (val error = state.sessionInfoError) {
            is SessionInfoError.ServerError -> MessageDialog(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .height(250.dp),
                image = painterResource(id = R.drawable.failure),
                titleDialog = "${error.code}",
                text = error.description,
                onDismissRequest = sessionVM::resetSessionInfoState
            )
            SessionInfoError.Unknown -> MessageDialog(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .height(250.dp),
                image = painterResource(id = R.drawable.failure),
                titleDialog = stringResource(R.string.err_ttl_dialog),
                text = stringResource(R.string.err_unknown_to_do_login),
                onDismissRequest = sessionVM::resetSessionInfoState
            )
        }
        SessionInfoUiState.Loading -> CircularProgressDialog()
        is SessionInfoUiState.Success -> TODO()
    }
}

@Composable
fun PanelSessionProfile(
    profileImage: Any?,
    name: String
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
            Spacer(modifier = Modifier.height(30.dp))
            AsyncImage(
                modifier = Modifier.size(50.dp),
                model = profileImage,
                contentDescription = stringResource(id = R.string.txt_cd_profile_image),
                placeholder = painterResource(id = R.drawable.user),
                error = painterResource(id = R.drawable.user)
            )
            DefaultText(
                modifier = Modifier.fillMaxWidth(),
                text = name,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun PanelSessionDetailSession(
    profileImage: Any?,
    name: String
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
            Spacer(modifier = Modifier.height(30.dp))
            AsyncImage(
                modifier = Modifier.size(50.dp),
                model = profileImage,
                contentDescription = stringResource(id = R.string.txt_cd_profile_image),
                placeholder = painterResource(id = R.drawable.user),
                error = painterResource(id = R.drawable.user)
            )
            DefaultText(
                modifier = Modifier.fillMaxWidth(),
                text = name,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}