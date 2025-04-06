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
fun DoctorHomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Doctor Dashboard") },
                actions = {
                    IconButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate(Screen.SignInScreen.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout"
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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()), // ✅ Makes it scrollable
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome, Doctor",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            HospitalCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionTitle("Your Daily Operations")
                    PrimaryButton(
                        text = "View Appointments",
                        onClick = { navController.navigate("doctor_appointments") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    PrimaryButton(
                        text = "Manage Availability",
                        onClick = { navController.navigate("manage_availability") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    PrimaryButton(
                        text = "Write Prescriptions",
                        onClick = { navController.navigate("write_prescription") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            HospitalCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionTitle("More Options")
                    PrimaryButton(
                        text = "Request Leave",
                        onClick = { navController.navigate("request_leave") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    PrimaryButton(
                        text = "Reschedule Appointments",
                        onClick = { navController.navigate("reschedule_appointments") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorLandingScreenPreview() {
    HospitalTheme {
        DoctorHomeScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorLandingScreenDarkPreview() {
    HospitalTheme(darkTheme = true) {
        DoctorHomeScreen(navController = rememberNavController())
    }
}
