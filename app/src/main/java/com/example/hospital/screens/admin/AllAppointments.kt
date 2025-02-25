package com.example.hospital.screens.admin

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hospital.data.models.Appointment
import com.example.hospital.data.repositories.AppointmentRepository
import androidx.compose.runtime.Composable
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllAppointmentsScreen() {
    val appointmentRepository = remember { AppointmentRepository() }
    var appointments by remember { mutableStateOf<List<Appointment>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        appointmentRepository.getAllAppointments(
            onSuccess = { fetchedAppointments ->
                appointments = fetchedAppointments
                isLoading = false
            },
            onFailure = { exception ->
                errorMessage = exception.message
                isLoading = false
                Log.e("AllAppointmentsScreen", "Error fetching appointments: ${exception.message}")
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("All Appointments") })
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize())
                }
                errorMessage != null -> {
                    Text(text = "Error: $errorMessage", modifier = Modifier.padding(16.dp))
                }
                appointments.isEmpty() -> {
                    Text(text = "No appointments found", modifier = Modifier.padding(16.dp))
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        items(appointments) { appointment ->
                            AppointmentItem(appointment)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppointmentItem(appointment: Appointment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Doctor: ${appointment.doctorName}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Patient: ${appointment.userName}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Time: ${appointment.appointmentTime}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AllAppointmentsScreenPreview() {
    val sampleAppointments = listOf(
        Appointment(
            id = "1",
            doctorId = "doc1",
            doctorName = "Dr. Smith",
            userId = "user1",
            userName = "John Doe",
            appointmentTime = Timestamp.now()
        ),
        Appointment(
            id = "2",
            doctorId = "doc2",
            doctorName = "Dr. Adams",
            userId = "user2",
            userName = "Jane Doe",
            appointmentTime = Timestamp.now()
        )
    )

    AllAppointmentsScreenPreviewContent(sampleAppointments)
}

@Composable
fun AllAppointmentsScreenPreviewContent(appointments: List<Appointment>) {
    AllAppointmentsScreenContent(appointments)
}

@Composable
fun AllAppointmentsScreenContent(appointments: List<Appointment>) {
    AllAppointmentsScreenContentPreview(appointments)
}

@Composable
fun AllAppointmentsScreenContentPreview(appointments: List<Appointment>) {
    AllAppointmentsScreen()
}
