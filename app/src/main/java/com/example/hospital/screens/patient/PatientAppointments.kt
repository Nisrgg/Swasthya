package com.example.hospital.screens.patient

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hospital.data.models.Appointment
import com.example.hospital.data.repositories.AppointmentRepository
import com.example.hospital.utils.DateUtils
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AppointmentsScreen() {
    val appointments = remember { mutableStateListOf<Appointment>() }
    val repository = AppointmentRepository()
    val userId = FirebaseAuth.getInstance().currentUser?.uid  // Get logged-in user's ID

    // Fetch only the logged-in user's appointments
    LaunchedEffect(userId) {
        if (userId != null) {
            repository.getAppointments(
                userId = userId, // Pass the user ID
                onSuccess = { fetchedAppointments ->
                    appointments.clear()
                    appointments.addAll(fetchedAppointments)
                },
                onFailure = { error ->
                    println("Error fetching appointments: ${error.message}")
                }
            )
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "My Appointments",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (appointments.isEmpty()) {
                EmptyStateCard(
                    message = "No upcoming appointments",
                    icon = "📅"
                )
            } else {
                appointments.forEach { appointment ->
                    AppointmentCard(appointment = appointment)
                }
            }
        }
    }
}

@Composable
private fun AppointmentCard(appointment: Appointment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = appointment.doctorName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

//            val dateText = appointment.appointmentTime?.toDate()?.let { date ->
//                SimpleDateFormat("EEEE, MMM dd 'at' hh:mm a", Locale.getDefault()).format(date)
//            } ?: "Invalid Date"

            val dateText = DateUtils.stamp(appointment.appointmentTime)


            Text(
                text = dateText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyStateCard(message: String, icon: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$icon  $message",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}