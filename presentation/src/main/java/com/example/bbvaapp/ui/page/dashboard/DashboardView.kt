package com.example.bbvaapp.ui.page.dashboard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
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
import com.example.bbvaapp.R
import com.example.bbvaapp.model.SessionInfoError
import com.example.bbvaapp.model.SessionInfoUiState
import com.example.bbvaapp.ui.BBVAFontFamily
import com.example.bbvaapp.ui.CircularProgressDialog
import com.example.bbvaapp.ui.DefaultText
import com.example.bbvaapp.ui.MessageDialog
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
                text = stringResource(R.string.err_unknown_to_fetch_session_info),
                onDismissRequest = sessionVM::resetSessionInfoState
            )
        }
        SessionInfoUiState.Loading -> CircularProgressDialog()
        is SessionInfoUiState.Success -> Dashboard(
            profileImage = state.userImage,
            name = state.userName,
            age = state.userAge,
            gender = state.userGender
        )
    }
}

@Composable
fun Dashboard(
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
            gender = gender
        )
        PanelSessionDetailSession(
            age = age,
            gender = gender
        )
        Spacer(modifier = Modifier.height(250.dp))
    }
}

@Composable
fun PanelSessionProfile(
    profileImage: Any?,
    name: String,
    gender: Gender
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
            Spacer(modifier = Modifier.height(20.dp))
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(3.dp, when (gender) {
                        Gender.MALE -> blue
                        Gender.FEMALE -> pink
                        Gender.UNDEFINED -> black
                    }, CircleShape),
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