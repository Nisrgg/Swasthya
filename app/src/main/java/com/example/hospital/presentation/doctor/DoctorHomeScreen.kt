package com.example.hospital.presentation.doctor

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospital.core.Screen
import com.example.hospital.core.theme.HospitalCard
import com.example.hospital.core.theme.HospitalTheme
import com.example.hospital.core.theme.SectionTitle
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.hospital.core.theme.PrimaryButton
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorHomeScreen(
    doctorId: String,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Doctor Dashboard",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate(Screen.SignInScreen.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Welcome Back, Doctor 👨‍⚕️",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 12.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Here’s your quick access panel.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            HospitalCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SectionTitle("Daily Tools")

                    PrimaryButton(
                        text = "🗓️ View Appointments",
                        onClick = { navController.navigate("all_appointments/$doctorId") }
                    )
                }
            }

            HospitalCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SectionTitle("Other Options")

                    PrimaryButton(
                        text = "📅 Request Leave",
                        onClick = { navController.navigate("all_leaves/$doctorId") }
                    )

                    PrimaryButton(
                        text = "🔁 Reschedule Appointments",
                        onClick = { navController.navigate("reschedule_appointment/$doctorId") }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorLandingScreenPreview() {
    HospitalTheme {
        DoctorHomeScreen("123", navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorLandingScreenDarkPreview() {
    HospitalTheme(darkTheme = true) {
        DoctorHomeScreen("123", navController = rememberNavController())
    }
}
