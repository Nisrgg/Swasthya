package com.example.hospital.presentation.patient

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.data.models.Doctor
import com.example.hospital.data.viewmodels.DoctorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorListScreen(viewModel: DoctorViewModel = viewModel()) {
    val doctors by viewModel.doctors.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Available Doctors") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(doctors) { doctor ->
                DoctorCard(doctor)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun DoctorCard(doctor: Doctor) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("👩‍⚕️ Dr. ${doctor.name}", style = MaterialTheme.typography.titleMedium)
            Text("Specialization: ${doctor.specialization}")
            Text("Experience: ${doctor.experience} years")
            Text("Education: ${doctor.education}")
            Text("Phone: ${doctor.phone}")
            Text("Email: ${doctor.email}")
            Text("Gender: ${doctor.gender}, Age: ${doctor.age}")

            Spacer(modifier = Modifier.height(8.dp))
            Text("Available Slots:")
            doctor.availableSlots.forEach { (day, slots) ->
                Text("• $day: ${slots.joinToString(", ")}")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewDoctorList() {
    DoctorListScreen()
}
