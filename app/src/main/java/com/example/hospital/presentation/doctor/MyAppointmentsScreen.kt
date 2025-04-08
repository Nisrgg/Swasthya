package com.example.hospital.presentation.doctor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hospital.core.theme.HospitalCard
import com.example.hospital.data.models.Appointment
import com.example.hospital.data.viewmodels.DoctorViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyAppointmentsScreen(
    doctorId: String,
    viewModel: DoctorViewModel
) {
    // Collect appointments state with default empty lists for safety.
    val allAppointments by viewModel.appointments.collectAsState(initial = emptyList())
    val weeklyAppointments by viewModel.upcomingAppointments.collectAsState(initial = emptyList())

    // Use a TabRow for the tab navigation
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Today", "Upcoming", "All")

    // Load data when the doctorId changes.
    LaunchedEffect(doctorId) {
        viewModel.loadAppointments(doctorId)
        viewModel.loadUpcomingAppointments(doctorId)
    }

    // Prepare date filtering.
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today = dateFormat.format(Date())

    val todaysAppointments = allAppointments.filter {
        dateFormat.format(it.data.appointment_date.toDate()) == today
    }

    val upcomingWeekAppointments = weeklyAppointments.filter {
        dateFormat.format(it.data.appointment_date.toDate()) != today
    }

    val allOtherAppointments = allAppointments.filter {
        dateFormat.format(it.data.appointment_date.toDate()) != today &&
                upcomingWeekAppointments.none { wa -> wa.id == it.id }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tabs using Material3 TabRow
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
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

        Spacer(modifier = Modifier.height(16.dp))

        // Choose list based on the selected tab
        val list = when (selectedTab) {
            0 -> todaysAppointments
            1 -> upcomingWeekAppointments
            else -> allOtherAppointments
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
    // Using your custom HospitalCard for consistent styling.
    HospitalCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Patient ID: ${appointment.patient_id}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(text = "Date: ${appointment.appointment_date}")
            Text(text = "Slot: ${appointment.slot}")
            Text(text = "Status: ${appointment.status}")
        }
    }
}

