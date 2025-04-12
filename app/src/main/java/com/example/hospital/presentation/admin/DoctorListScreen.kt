package com.example.hospital.presentation.admin

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospital.data.models.Doctor
import com.example.hospital.data.viewmodels.DoctorViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorListScreen(
    specialization: String,
    doctorIds: List<String>,
    navController: NavController,
    viewModel: DoctorViewModel = viewModel()
) {
    // Collect the doctors state from the ViewModel.
    val doctors by viewModel.doctors.collectAsState()

    // Fetch doctors when the list of IDs changes.
    LaunchedEffect(doctorIds) {
        viewModel.fetchDoctorsBySpecialization(doctorIds)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Doctors - $specialization") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(doctors) { doctor ->
                DoctorCard(doctor = doctor) {
                    navController.navigate("doctor_preview/${doctor.id}")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoctorCard(doctor: Doctor, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "👨‍⚕️ Dr. ${doctor.name}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            // You can add additional details below if desired:
            // For example:
            // Text(text = doctor.specialization, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PreviewDoctorList() {
    // Provide dummy or empty list for preview.
    DoctorListScreen(
        specialization = "Dermatologist",
        doctorIds = emptyList(),
        navController = rememberNavController()
    )
}