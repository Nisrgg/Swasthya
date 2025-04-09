package com.example.hospital.presentation.doctor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hospital.core.theme.HospitalCard
import com.example.hospital.data.models.Appointment
import com.example.hospital.data.viewmodels.DoctorViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyAppointmentsScreen(
    doctorId: String,
    viewModel: DoctorViewModel
) {
    val allAppointments by viewModel.appointments.collectAsState(initial = emptyList())
    val weeklyAppointments by viewModel.upcomingAppointments.collectAsState(initial = emptyList())

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Today", "Upcoming", "Past")

    val statusOptions = listOf("All", "Pending", "Completed", "Cancelled","confirmed","rescheduled")
    var selectedStatus by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf(false) }


    LaunchedEffect(doctorId) {
        viewModel.loadAppointments(doctorId)
        viewModel.loadUpcomingAppointments(doctorId)
    }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today = dateFormat.format(Date())

    val todaysAppointments = allAppointments
        .filter { dateFormat.format(it.data.appointment_date.toDate()) == today }
        .sortedBy { it.data.appointment_date.toDate() }

    val upcomingAppointments = weeklyAppointments
        .filter { dateFormat.format(it.data.appointment_date.toDate()) > today }
        .sortedBy { it.data.appointment_date.toDate() }

    val pastAppointments = allAppointments
        .filter { dateFormat.format(it.data.appointment_date.toDate()) < today }
        .sortedByDescending { it.data.appointment_date.toDate() } // Past: latest first

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Status: $selectedStatus")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                statusOptions.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status) },
                        onClick = {
                            selectedStatus = status
                            expanded = false
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        val rawList = when (selectedTab) {
            0 -> todaysAppointments
            1 -> upcomingAppointments
            2 -> pastAppointments
            else -> emptyList()
        }

        // Apply status filter
        val list = rawList.filter {
            selectedStatus == "All" || it.data.status.equals(selectedStatus, ignoreCase = true)
        }

        if (list.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No appointments found.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(list) { appointmentWithId ->
                    AppointmentCard(appointment = appointmentWithId.data)
                }
            }
        }


    }
}

@Composable
fun AppointmentCard(appointment: Appointment) {
    // Convert timestamp to formatted date
    val formattedDate = remember(appointment.appointment_date) {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        sdf.format(appointment.appointment_date.toDate())
    }

    HospitalCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Patient Name: ${appointment.patient_name}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(text = "Date: $formattedDate")
            Text(text = "Slot: ${appointment.slot}")
            Text(text = "Status: ${appointment.status}")
        }
    }
}