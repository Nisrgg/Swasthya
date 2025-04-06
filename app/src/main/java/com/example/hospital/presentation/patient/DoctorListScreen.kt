package com.example.hospital.presentation.patient

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
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorListScreen(
    specialization: String,
    doctorIds: List<String>,
    navController: NavController,
    viewModel: DoctorViewModel = viewModel()
) {
    val doctors by viewModel.doctors.collectAsState()

    // fetch doctors by provided Firestore document IDs
    LaunchedEffect(doctorIds) {
        viewModel.fetchDoctorsBySpecialization(doctorIds)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Doctors - $specialization") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(doctors) { doctor ->
                DoctorCard(doctor = doctor) {
                    navController.navigate("doctor_preview/${doctor.id}")
                }
                Spacer(modifier = Modifier.height(12.dp))
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("👩‍⚕️ Dr. ${doctor.name}", style = MaterialTheme.typography.titleMedium)
            Text("Experience: ${doctor.experience} years")

            val today = LocalDate.now().dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
            val isAvailable = doctor.available_slots.containsKey(today)

            Text("Available Today: ${if (isAvailable) "✅ Yes" else "❌ No"}")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PreviewDoctorList() {
    DoctorListScreen("Dermatologist", listOf(), rememberNavController())
}