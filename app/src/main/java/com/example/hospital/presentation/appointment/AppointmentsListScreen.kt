package com.example.hospital.presentation.appointment

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hospital.data.models.Appointment
import com.example.hospital.data.models.Doctor
import com.example.hospital.data.viewmodels.AppointmentDoctorViewModel
import com.example.hospital.data.viewmodels.AppointmentViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AppointmentsListScreen(
    viewModel: AppointmentViewModel = viewModel(),
    doctorViewModel: AppointmentDoctorViewModel = viewModel()
) {
    val doctorMap by doctorViewModel.doctorMap
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val patientId = remember { mutableStateOf<String?>(null) }
    val nowDate = remember { Date() }
    val appointments by viewModel.appointments

    // Fetch patient ID and appointments when the composable is first launched.
    LaunchedEffect(true) {
        auth.currentUser?.uid?.let { uid ->
            patientId.value = uid
            viewModel.fetchAppointments(uid) // Trigger fetching appointments
        } ?: run {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    // When appointments update, fetch the associated doctors.
    LaunchedEffect(appointments) {
        if (appointments.isNotEmpty()) {
            doctorViewModel.fetchDoctorsForAppointments(appointments)
        }
    }

    // Separate appointments into upcoming and past appointments.
    val upcomingAppointments = appointments.filter { it.appointment_date.toDate().after(nowDate) }
    val pastAppointments = appointments.filter { it.appointment_date.toDate().before(nowDate) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Upcoming Appointments",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (upcomingAppointments.isEmpty()) {
            Text(
                text = "No upcoming appointments.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn {
                items(upcomingAppointments) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        doctor = doctorMap[appointment.doctor_id]
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Finished Appointments",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (pastAppointments.isEmpty()) {
            Text(
                text = "No finished appointments.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn {
                items(pastAppointments) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        doctor = doctorMap[appointment.doctor_id]
                    )
                }
            }
        }
    }
}

@Composable
fun AppointmentCard(appointment: Appointment, doctor: Doctor?) {
    // Format appointment date.
    val formattedDate = remember(appointment.appointment_date) {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        sdf.format(appointment.appointment_date.toDate())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Doctor's name header.
            Text(
                text = "👨‍⚕️ Dr. ${doctor?.name ?: "Doctor"}",
                style = MaterialTheme.typography.titleMedium
            )

            // Specialization (if available).
            doctor?.specialization?.let {
                Text(
                    text = "Specialization: $it",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Date and Slot info.
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📅 Date", style = MaterialTheme.typography.labelMedium)
                Text(formattedDate, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🕐 Slot", style = MaterialTheme.typography.labelMedium)
                Text(appointment.slot, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Appointment status (if provided).
            appointment.status?.let {
                Text(
                    text = "Status: ${it.replaceFirstChar { char -> char.uppercase() }}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Display prescription details if available.
            if (appointment.prescription.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Text(
                    text = "💊 Prescription",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
                )
                Text(
                    text = appointment.prescription,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}