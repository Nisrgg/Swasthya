package com.example.hospital.presentation.patient

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hospital.data.viewmodels.UserProfileViewModel
import com.example.hospital.presentation.admin.OnboardingScreen

@Composable
fun UserProfileScreen(
    userId: String,
    navController: NavController,
    viewModel: UserProfileViewModel = viewModel(),
    onProfileUpdated: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        if (viewModel.userProfile == null && !viewModel.isLoading) {
            viewModel.loadUserProfile(userId)
        }
    }
    val profile = viewModel.userProfile

    if (profile == null || viewModel.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
        return
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())) {

        Text("Your Profile", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        if (isEditing) {
            // You can reuse Onboarding form components here with prefilled values
            // For brevity, just re-call OnboardingScreen style logic
            OnboardingScreen(userId = userId, navController = navController, viewModel = viewModel, onProfileSaved = {
                isEditing = false
                onProfileUpdated()
            })
        } else {
            Spacer(Modifier.height(16.dp))
            Text("Name: ${profile.name}")
            Text("Age: ${profile.age}")
            Text("Height: ${profile.height}")
            Text("Weight: ${profile.weight}")
            Text("Blood Group: ${profile.blood_group}")
            if (profile.has_family_history) Text("Family History: ${profile.family_history_details}")
            if (profile.has_allergies) Text("Allergies: ${profile.allergy_details}")
            if (profile.has_medications) Text("Medications: ${profile.medication_details}")

            Button(
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("editProfile", profile)
                    navController.navigate("post_login/$userId")
                },
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
            ) {
                Text("Edit Profile")
            }
        }
    }
}


@Composable
fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}