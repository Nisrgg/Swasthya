package com.example.hospital.presentation.admin

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.data.models.Appointment
import com.example.hospital.data.viewmodels.DoctorViewModel
import com.google.firebase.Timestamp
import java.util.*
import android.util.Log

@Composable
fun RescheduleAppointmentScreen(
    doctorId: String,
    viewModel: DoctorViewModel = viewModel()
) {
    val appointments by viewModel.upcomingAppointments.collectAsState()
    var selectedAppointment by remember { mutableStateOf<Pair<String, Appointment>?>(null) }
    var selectedDate by remember { mutableStateOf<Timestamp?>(null) }
    var selectedSlot by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(doctorId) {
        Log.d("DEBUG", "Loading appointments for: $doctorId")
        viewModel.loadUpcomingAppointments(doctorId)
    }

    // Show Date Picker when triggered
    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(year, month, day)
                selectedDate = Timestamp(calendar.time)
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Upcoming Appointments", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        appointments.forEach { (id, appointment) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        selectedAppointment = id to appointment
                        showDialog = true
                    },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Patient: ${appointment.patient_name}")
                    Text("Date: ${appointment.appointment_date.toDate()}")
                    Text("Slot: ${appointment.slot}")
                    Text("Status: ${appointment.status}")
                }
            }
        }
    }

    // Show Alert Dialog for Rescheduling
    if (showDialog && selectedAppointment != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val (id, _) = selectedAppointment!!
                    val newDate = selectedDate ?: Timestamp.now()
                    viewModel.rescheduleAppointment(id, newDate, selectedSlot) {
                        showDialog = false
                        selectedDate = null
                        selectedSlot = ""
                        selectedAppointment = null

                        viewModel.loadUpcomingAppointments(doctorId)
                    }
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    selectedDate = null
                    selectedSlot = ""
                }) {
                    Text("Cancel")
                }
            },
            title = { Text("Reschedule Appointment") },
            text = {
                Column {
                    Text("Pick a new date:")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { showDatePicker = true }) {
                        Text(selectedDate?.toDate()?.toString() ?: "Select Date")
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Pick a new time slot:")
                    Spacer(Modifier.height(8.dp))
                    TextField(
                        value = selectedSlot,
                        onValueChange = { selectedSlot = it },
                        label = { Text("Slot (e.g. 10:00 - 10:30)") }
                    )
                }
            }
        )
    }
}