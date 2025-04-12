package com.example.hospital.presentation.admin

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.clickable
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
import com.example.hospital.data.models.Appointment
import com.example.hospital.data.viewmodels.DoctorViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RescheduleAppointmentScreen(
    doctorId: String,
    viewModel: DoctorViewModel = viewModel()
) {
    val context = LocalContext.current

    // Collect upcoming appointments as a map (id -> Appointment).
    val appointments by viewModel.upcomingAppointments.collectAsState()

    // Local UI states.
    var selectedAppointment by remember { mutableStateOf<Pair<String, Appointment>?>(null) }
    var newDate by remember { mutableStateOf<Date?>(null) }
    var newSlot by remember { mutableStateOf("") }
    var showRescheduleDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Load appointments when the screen is displayed.
    LaunchedEffect(doctorId) {
        Log.d("Reschedule", "Loading upcoming appointments for: $doctorId")
        viewModel.loadUpcomingAppointments(doctorId)
    }

    // If triggered, show the DatePickerDialog.
    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(year, month, day)
                newDate = calendar.time
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Main screen layout.
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reschedule Appointments", style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Select an appointment to reschedule", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Display each upcoming appointment as a clickable card.
                items(appointments.toList()) { (id, appointment) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedAppointment = id to appointment
                                // Initialize the dialog with current appointment details.
                                newDate = appointment.appointment_date.toDate()
                                newSlot = appointment.slot
                                showRescheduleDialog = true
                            },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Patient: ${appointment.patient_name}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Date: ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(appointment.appointment_date.toDate())}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Slot: ${appointment.slot}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }

    // Reschedule Dialog.
    if (showRescheduleDialog && selectedAppointment != null) {
        AlertDialog(
            onDismissRequest = { showRescheduleDialog = false },
            title = { Text("Reschedule Appointment") },
            text = {
                Column {
                    Text("Select a new date:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = newDate?.let {
                                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(it)
                            } ?: "Select Date"
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Enter a new time slot:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newSlot,
                        onValueChange = { newSlot = it },
                        label = { Text("Time Slot (e.g. 10:00 - 10:30)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    // Use newDate if available; otherwise, default to now.
                    val rescheduledDate = newDate?.let { Timestamp(it) } ?: Timestamp.now()
                    selectedAppointment?.let { (id, _) ->
                        viewModel.rescheduleAppointment(id, rescheduledDate, newSlot) {
                            // After successful reschedule, reset state and refresh appointments.
                            showRescheduleDialog = false
                            selectedAppointment = null
                            newDate = null
                            newSlot = ""
                            viewModel.loadUpcomingAppointments(doctorId)
                        }
                    }
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showRescheduleDialog = false
                    selectedAppointment = null
                    newDate = null
                    newSlot = ""
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}