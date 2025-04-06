package com.example.hospital.presentation.patient

import android.app.DatePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospital.data.models.Appointment
import com.example.hospital.data.repositories.AppointmentRepository
import com.example.hospital.data.viewmodels.DoctorViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import android.util.Log;

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DoctorPreviewScreen(
    doctorId: String,
    navController: NavController,
    viewModel: DoctorViewModel = viewModel(),
    appointmentRepo: AppointmentRepository = AppointmentRepository()
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedSlot by remember { mutableStateOf<String?>(null) }

    val doctor = viewModel.selectedDoctor.value
    val context = LocalContext.current

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

                Text("Weekly Availability:", style = MaterialTheme.typography.titleMedium)

                val weekDays = listOf(
                    "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
                )

                val availableSlotsSorted = doctor.available_slots
                    .filter { it.value.isNotEmpty() }
                    .toSortedMap(compareBy { day ->
                        weekDays.indexOf(day.replaceFirstChar { it.uppercase() })
                    })

                if (availableSlotsSorted.isNotEmpty()) {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        availableSlotsSorted.forEach { (day, slots) ->
                            Text("✅ ${day.replaceFirstChar { it.uppercase() }}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                } else {
                    Text("No weekly availability set.", style = MaterialTheme.typography.bodySmall)
                }



                Spacer(modifier = Modifier.height(24.dp))

                Text("Choose a date:", style = MaterialTheme.typography.titleMedium)
                DatePicker(selectedDate) { newDate ->
                    selectedDate = newDate
                    selectedSlot = null // Reset selected slot on date change
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Available Slots on $dayOfWeek", style = MaterialTheme.typography.titleMedium)
                if (slots.isNotEmpty()) {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        slots.forEach { slot ->
                            SelectableChip(
                                label = slot,
                                isSelected = slot == selectedSlot,
                                onClick = { selectedSlot = slot }
                            )
                        }
                    }

                    selectedSlot?.let { slot ->
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            Log.d("SlotCheck", "Selected slot: $slot")

                            val timestamp = parseTimeToTimestamp(selectedDate, slot)

                            val patientId = FirebaseAuth.getInstance().currentUser?.uid
                            Log.d("FirebaseCheck", "Patient ID: $patientId")


                            if (timestamp != null &&  patientId != null) {
                                val appointment = Appointment(
                                    appointment_date = timestamp,
                                    doctor_id = doctorId,
                                    patient_id = patientId,
                                    prescription = "",
                                    slot = slot,
                                    status = "pending"
                                )

                                appointmentRepo.bookAppointment(appointment) { success ->
                                    if (success) {
                                        Toast.makeText(context, "Appointment booked!", Toast.LENGTH_SHORT).show()
                                        navController.navigate("dashboard")
                                    } else {
                                        Toast.makeText(context, "Booking failed. Try again.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Error parsing time or user not logged in", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Text("Book Appointment at $slot")
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
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun SelectableChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(backgroundColor, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Text(text = label, color = Color.White)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(selectedDate: LocalDate, onDateChange: (LocalDate) -> Unit) {
    val context = LocalContext.current
    val datePickerDialog = remember {
        DatePickerDialog(
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

@RequiresApi(Build.VERSION_CODES.O)
fun parseTimeToTimestamp(date: LocalDate, timeStr: String): Timestamp? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH) // 👈 use ENGLISH
        val time = java.time.LocalTime.parse(timeStr.trim(), formatter)
        val dateTime = date.atTime(time)
        val instant = dateTime.atZone(ZoneId.systemDefault()).toInstant()
        Timestamp(Date.from(instant))
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("TimeParseError", "Failed to parse time: '$timeStr'")
        null
    }
}
