package com.example.hospital.presentation.patient

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hospital.data.viewmodels.UserProfileViewModel

@Composable
fun UserProfileScreen(
    userId: String,
    navController: NavController,
    viewModel: UserProfileViewModel = viewModel()
) {
    val profile by remember { derivedStateOf { viewModel.userProfile } }

    LaunchedEffect(userId) {
        viewModel.loadUserProfile(userId)
    }

    if (profile.name.isEmpty()) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("👤 ${profile.name}", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))

            ProfileRow("Age", profile.age)
            ProfileRow("Height", "${profile.height} cm")
            ProfileRow("Weight", "${profile.weight} kg")
            ProfileRow("Blood Group", profile.blood_group)
            ProfileRow("Family History", profile.family_medical_history)
            ProfileRow("Allergies", profile.allergies)
            ProfileRow("Medications", profile.ongoing_medications)

            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                navController.navigate("edit_user_profile")
            }) {
                Text("✏️ Edit Profile")
            }
        }
    }
}

@Composable
fun ProfileRow(label: String, value: String) {
    Row(Modifier.padding(vertical = 4.dp)) {
        Text("$label: ", style = MaterialTheme.typography.titleSmall)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}