package com.example.bbvaapp.ui.page.dashboard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bbvaapp.R
import com.example.bbvaapp.model.CloseSessionUiState
import com.example.bbvaapp.model.SessionInfoError
import com.example.bbvaapp.model.SessionInfoUiState
import com.example.bbvaapp.ui.BBVAFontFamily
import com.example.bbvaapp.ui.CircularProgressDialog
import com.example.bbvaapp.ui.DefaultImageButton
import com.example.bbvaapp.ui.DefaultText
import com.example.bbvaapp.ui.MessageDialog
import com.example.bbvaapp.ui.navigation.Route
import com.example.bbvaapp.ui.theme.black
import com.example.bbvaapp.ui.theme.blue
import com.example.bbvaapp.ui.theme.pink
import com.example.bbvaapp.utils.MessageResolver
import com.example.bbvaapp.vm.SessionVM
import com.example.domain.entities.Gender

@Composable
fun DashboardView(
    sessionVM: SessionVM,
    navController: NavHostController,
    messageResolver: MessageResolver
) {
    val sessionInfoState: SessionInfoUiState? by sessionVM.sessionInfoState.collectAsStateWithLifecycle()
    if (sessionInfoState == null) return
    when (val state = sessionInfoState as SessionInfoUiState) {
        is SessionInfoUiState.ErrorToLoadImage -> Dashboard2(
            sessionVM = sessionVM,
            userId = state.userId,
            userName = state.userName,
            userGender = state.userGender,
            userAge = state.userAge
        )
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
                text = stringResource(R.string.err_to_fetch_session_info),
                onDismissRequest = sessionVM::resetSessionInfoState
            )
        }
        SessionInfoUiState.Loading -> CircularProgressDialog()
        is SessionInfoUiState.Success -> Dashboard(
            sessionVM = sessionVM,
            userId = state.userId,
            profileImage = state.userImage,
            name = state.userName,
            age = state.userAge,
            gender = state.userGender
        )
    }
    LogoutState(
        sessionVM = sessionVM,
        navController = navController
    )
}

@Composable
fun Dashboard(
    sessionVM: SessionVM,
    userId: String,
    profileImage: Any?,
    name: String,
    age: Int,
    gender: Gender,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(250.dp))
        PanelSessionProfile(
            profileImage = profileImage,
            name = name,
            gender = gender,
        ) {
            sessionVM.logout(userId)
        }
        PanelSessionDetailSession(
            age = age,
            gender = gender
        )
        Spacer(modifier = Modifier.height(250.dp))
    }
}

@Composable
fun Dashboard2(
    sessionVM: SessionVM,
    userId: String,
    userName: String,
    userGender: Gender,
    userAge: Int
) {
    var showErrorImage by rememberSaveable { mutableStateOf(true) }

    if (showErrorImage) {
        MessageDialog(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .height(250.dp),
            image = painterResource(id = R.drawable.failure),
            titleDialog = stringResource(R.string.err_ttl_dialog),
            text = stringResource(R.string.err_to_fetch_image)
        ) {
            showErrorImage = false
        }
    }

    Dashboard(
        sessionVM = sessionVM,
        userId = userId,
        profileImage = ImageRequest.Builder(LocalContext.current)
            .data(when (userGender) {
                Gender.MALE -> R.drawable.male
                Gender.FEMALE -> R.drawable.female
                Gender.UNDEFINED -> R.drawable.user
            })
            .crossfade(true)
            .build(),
        name = userName,
        age = userAge,
        gender = userGender
    )
}

@Composable
fun PanelSessionProfile(
    profileImage: Any?,
    name: String,
    gender: Gender,
    logout: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                DefaultImageButton(
                    imageSize = 32.dp,
                    iconButton = Icons.AutoMirrored.Filled.Logout,
                    cdIconButton = R.string.txt_cd_btn_logout,
                    iconColor = Color.Red,
                    onClickButton = logout
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(
                        3.dp, when (gender) {
                            Gender.MALE -> blue
                            Gender.FEMALE -> pink
                            Gender.UNDEFINED -> black
                        }, CircleShape
                    ),
                model = profileImage,
                contentDescription = stringResource(id = R.string.txt_cd_profile_image),
                placeholder = painterResource(id = R.drawable.user),
                error = painterResource(id = R.drawable.user),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                fontSize = dimensionResource(
                    id = R.dimen.title_text_size
                ).value.sp,
                text = name,
                fontFamily = BBVAFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun PanelSessionDetailSession(
    age: Int,
    gender: Gender,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 20.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                fontSize = dimensionResource(
                    id = R.dimen.title_text_size
                ).value.sp,
                text = stringResource(R.string.txt_ttl_info_detail_session),
                fontFamily = BBVAFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            DefaultText(
                modifier = Modifier.fillMaxWidth(),
                text = "${stringResource(R.string.txt_label_user_age)} $age",
            )
            Spacer(modifier = Modifier.height(10.dp))
            DefaultText(
                modifier = Modifier.fillMaxWidth(),
                text = "${stringResource(R.string.txt_label_user_gender)} ${gender.id}",
            )
            Spacer(modifier = Modifier.height(10.dp))
            DefaultText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.txt_label_user_description),
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun LogoutState(
    sessionVM: SessionVM,
    navController: NavHostController
) {
    val closeSessionState: CloseSessionUiState? by sessionVM.closeSessionState.collectAsStateWithLifecycle()
    if (closeSessionState == null) return
    when (val state: CloseSessionUiState = closeSessionState as CloseSessionUiState) {
        is CloseSessionUiState.Fail -> MessageDialog(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .height(250.dp),
            image = painterResource(id = R.drawable.failure),
            titleDialog = stringResource(R.string.err_ttl_dialog),
            text = stringResource(R.string.err_to_close_session),
            onDismissRequest = sessionVM::resetCloseSessionState
        )
        is CloseSessionUiState.Loading -> CircularProgressDialog()
        is CloseSessionUiState.Success -> LaunchedEffect(true) {
            navController.navigate(Route.Login.route) {
                popUpTo(Route.Dashboard.createRoute(state.userId)) {
                    inclusive = true
                }
            }
        }
    }
}