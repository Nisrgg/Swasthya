package com.example.hospital.presentation.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.data.viewmodels.DoctorViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DoctorPreviewScreen(
    doctorId: String,
    viewModel: DoctorViewModel = viewModel()
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val doctor = viewModel.selectedDoctor.value

    // Fetch doctor data
    LaunchedEffect(doctorId) {
        viewModel.fetchDoctorById(doctorId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(doctor?.name ?: "Doctor Details")
            })
        }
    ) { padding ->
        doctor?.let { it ->
            val dayOfWeek = selectedDate.dayOfWeek.name.lowercase()
                .replaceFirstChar { it.uppercase() }

            val slots = it.available_slots[dayOfWeek] ?: emptyList()

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Name: ${it.name}", style = MaterialTheme.typography.titleLarge)
                Text("Specialization: ${it.specialization}")
                Text("Experience: ${it.experience} years")
                Text("Education: ${it.education}")
                Text("Contact: ${it.phone}")
                Text("Email: ${it.email}")
                Text("Gender: ${it.gender}")
                Text("Age: ${it.age}")

                Spacer(modifier = Modifier.height(24.dp))

                Text("Choose a date:", style = MaterialTheme.typography.titleMedium)
                DatePicker(selectedDate) { newDate -> selectedDate = newDate }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Available Slots on $dayOfWeek", style = MaterialTheme.typography.titleMedium)
                if (slots.isNotEmpty()) {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        slots.forEach { slot ->
                            Chip(label = slot)
                        }
                    }
                } else {
                    Text("No slots available.")
                }
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun Chip(label: String) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = label, color = Color.White)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(selectedDate: LocalDate, onDateChange: (LocalDate) -> Unit) {
    val context = LocalContext.current
    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, day ->
                onDateChange(LocalDate.of(year, month + 1, day))
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )
    }

    Button(onClick = { datePickerDialog.show() }) {
        Text("Select Date: ${selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}")
    }
}
