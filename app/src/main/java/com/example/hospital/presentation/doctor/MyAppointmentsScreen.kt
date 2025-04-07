//package com.example.hospital.presentation.doctor
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import com.example.hospital.data.models.Appointment
//import com.example.hospital.data.viewmodels.DoctorViewModel
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun MyAppointmentsScreen(
//    doctorId: String,
//    viewModel: DoctorViewModel
//) {
//    val allAppointments by viewModel.appointments.collectAsState()
//    val weeklyAppointments by viewModel.weeklyAppointments.collectAsState()
//
//    var selectedTab by remember { mutableStateOf(0) }
//
//    // Load data
//    LaunchedEffect(doctorId) {
//        viewModel.loadAppointments(doctorId)
//        viewModel.loadWeeklyAppointments(doctorId)
//    }
//
//    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//    val today = dateFormat.format(Date())
//
//    val todaysAppointments = allAppointments.filter {
//        dateFormat.format(it.appointment_date.toDate()) == today
//    }
//
//    val upcomingWeekAppointments = weeklyAppointments.filter {
//        dateFormat.format(it.appointment_date.toDate()) != today
//    }
//
//    val allOtherAppointments = allAppointments.filter {
//        dateFormat.format(it.appointment_date.toDate()) != today &&
//                upcomingWeekAppointments.none { wa -> wa.appointment_id == it.appointment_id }
//    }
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//
//        // Top Tabs
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            val tabs = listOf("Today", "Upcoming", "All")
//            tabs.forEachIndexed { index, title ->
//                Button(
//                    onClick = { selectedTab = index },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = if (selectedTab == index) MaterialTheme.colorScheme.primary else Color.LightGray
//                    )
//                ) {
//                    Text(text = title, color = Color.White)
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        val list = when (selectedTab) {
//            0 -> todaysAppointments
//            1 -> upcomingWeekAppointments
//            else -> allOtherAppointments
//        }
//
//        if (list.isEmpty()) {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text("No appointments found.")
//            }
//        } else {
//            LazyColumn {
//                items(list) { appointment ->
//                    AppointmentCard(appointment)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun AppointmentCard(appointment: Appointment) {
//    Card(
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text("Patient ID: ${appointment.patient_id}", fontWeight = FontWeight.Bold)
//            Text("Date: ${appointment.appointment_date}")
//            Text("Slot: ${appointment.slot}")
//            Text("Status: ${appointment.status}")
//        }
//    }
//}
