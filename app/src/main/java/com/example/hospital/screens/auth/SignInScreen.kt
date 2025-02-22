package com.example.hospital.screens.auth

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
import com.example.hospital.ui.theme.*
import androidx.compose.material3.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight

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
            .background(gradientBrush) // Use gradient background
            .systemBarsPadding()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.login_blur),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f // Make the background image less prominent
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Logo Section
            HospitalCard(
                modifier = Modifier
                    .size(120.dp)
                    .clip(MaterialTheme.shapes.large)
                    .shadow(8.dp, MaterialTheme.shapes.large) // Add shadow for depth
            ) {
                Image(
                    painter = painterResource(id = R.drawable.oig4__rndcloiljdx4hxpn),
                    contentDescription = "App Logo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title Section
            Text(
                text = "Swasthya",
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Connect securely with your healthcare team",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Buttons Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                // Google Sign In Button
                PrimaryButton(
                    text = "Continue with Google",
                    onClick = onSignInClick,
                    modifier = Modifier.fillMaxWidth(),
                )

                // Admin Button
                Button(
                    onClick = { navController.navigate("admin_screen") },
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp) // Add elevation
                ) {
                    Text(
                        text = "Admin",
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