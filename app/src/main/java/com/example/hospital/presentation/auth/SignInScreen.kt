package com.example.hospital.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospital.R
import androidx.compose.material3.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import com.example.hospital.core.theme.HospitalCard
import com.example.hospital.core.theme.HospitalTheme
import com.example.hospital.core.theme.PrimaryButton
import com.example.hospital.core.theme.md_theme_light_primary
import com.example.hospital.core.theme.md_theme_light_secondary

@Composable
fun SignInScreenUI(
    navController: NavController,
    onSignInClick: () -> Unit
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            md_theme_light_primary,
            md_theme_light_secondary
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .systemBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_blur),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.3f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            HospitalCard(
                modifier = Modifier
                    .size(120.dp)
                    .clip(MaterialTheme.shapes.large)
                    .shadow(8.dp, MaterialTheme.shapes.large)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_hospital_logo),
                    contentDescription = "App Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Swaasthya",
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Connect securely with your Healthcare Team",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                PrimaryButton(
                    text = "Continue with Google",
                    onClick = onSignInClick,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedButton(
                    onClick = { navController.navigate("doctor_login") },
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(48.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Doctor",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    HospitalTheme {
        val navController = rememberNavController()
        SignInScreenUI(navController, {})
    }
}