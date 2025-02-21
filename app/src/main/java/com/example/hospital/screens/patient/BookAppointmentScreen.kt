package com.example.hospital.screens.patient

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hospital.data.repositories.AppointmentRepository
import com.example.hospital.data.models.Doctor
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BookAppointmentScreen(
    navController: NavController,
    doctorId: String,
    appointmentRepository: AppointmentRepository = AppointmentRepository()
) {
    val db = FirebaseFirestore.getInstance()
    var doctor by remember { mutableStateOf<Doctor?>(null) }
    var selectedDate by remember { mutableStateOf<String?>(null) } // 📅 Selected Date
    var availableSlots by remember { mutableStateOf<List<String>>(emptyList()) } // ⏰ Slots for Date
    var selectedSlot by remember { mutableStateOf<String?>(null) }

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val userId = user?.uid ?: "unknown_user"
    val userName = user?.displayName ?: "Anonymous"

    // Fetch doctor details
    LaunchedEffect(doctorId) {
        db.collection("doctors").document(doctorId).get().addOnSuccessListener { doc ->
            doctor = doc.toObject(Doctor::class.java)?.copy(id = doc.id)
        }
    }

    BookAppointmentContent(
        doctor = doctor,
        selectedDate = selectedDate,
        onDateSelected = { date ->
            selectedDate = date
            availableSlots = doctor?.availableSlots?.get(date) ?: emptyList() // ✅ Fetch slots for selected date
        },
        availableSlots = availableSlots,
        selectedSlot = selectedSlot,
        onSlotSelected = { selectedSlot = it },
        onConfirmAppointment = {
            val userId = FirebaseAuth.getInstance().currentUser?.uid  // ✅ Get the logged-in user's ID

            selectedSlot?.let { time ->
                val selectedTimestamp = convertSlotToTimestamp(selectedDate, time)  // ✅ Convert date+time to Timestamp

                if (userId != null) {  // ✅ Ensure user is logged in
                    appointmentRepository.bookAppointment(
                        doctorId = doctorId,
                        doctorName = doctor?.name ?: "",
                        userId = userId,  // ✅ Pass userId here
                        userName = userName,
                        appointmentTime = selectedTimestamp, // ✅ Use converted timestamp
                        onSuccess = { navController.popBackStack() },
                        onFailure = { e -> println("Error booking appointment: ${e.message}") }
                    )
                } else {
                    println("Error: User not logged in")  // Debugging log
                }
            }
        }

    )
}

@Composable
private fun BookAppointmentContent(
    doctor: Doctor?,
    selectedDate: String?,
    onDateSelected: (String) -> Unit,
    availableSlots: List<String>,
    selectedSlot: String?,
    onSlotSelected: (String) -> Unit,
    onConfirmAppointment: () -> Unit,
    modifier: Modifier = Modifier
) {
    doctor?.let { doc ->
        Column(modifier = modifier.padding(16.dp)) {
            Text(
                text = "Book Appointment with ${doc.name}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 📅 Date Picker
            DatePicker(onDateSelected = onDateSelected)

            Spacer(modifier = Modifier.height(16.dp))

            // ⏰ Time Slot Selection
            if (availableSlots.isNotEmpty()) {
                LazyColumn {
                    items(availableSlots.size) { index ->
                        val slot = availableSlots[index]

                        Button(
                            onClick = { onSlotSelected(slot) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (slot == selectedSlot)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(
                                text = slot,
                                color = if (slot == selectedSlot)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = if (selectedDate != null) "No slots available for this date" else "Select a date",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onConfirmAppointment,
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedDate != null && selectedSlot != null
            ) {
                Text(text = "Confirm Appointment")
            }
        }
    } ?: Text(
        text = "Loading...",
        modifier = Modifier.fillMaxSize(),
        textAlign = TextAlign.Center
    )
}

// 📅 Date Picker Composable
@Composable
fun DatePicker(onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Button(onClick = {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                onDateSelected(selectedDate) // ✅ Set selected date
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }) {
        Text(text = "Select Date")
    }
}

// ✅ Convert Date + Time to Timestamp
fun convertSlotToTimestamp(date: String?, time: String): Timestamp {
    if (date == null) throw IllegalArgumentException("Date cannot be null")

    val dateTimeString = "$date $time" // e.g., "2025-02-22 9:00 AM"
    val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.US)
    val dateObj = dateTimeFormat.parse(dateTimeString)!!

    return Timestamp(dateObj) // ✅ Convert to Firebase Timestamp
}

@Preview(showBackground = true)
@Composable
fun BookAppointmentPreview() {
    val mockDoctor = Doctor(
        id = "preview_id",
        name = "Dr. John Doe",
        specialization = "Cardiologist",
        availableSlots = mapOf(
            "2025-02-22" to listOf("9:00 AM", "10:00 AM", "2:00 PM"),
            "2025-02-23" to listOf("11:00 AM", "3:00 PM")
        )
    )

    var selectedSlot by remember { mutableStateOf<String?>(null) }

    MaterialTheme {
        BookAppointmentContent(
            doctor = mockDoctor,
            selectedDate = "2025-02-22",
            onDateSelected = {},
            availableSlots = mockDoctor.availableSlots["2025-02-22"] ?: emptyList(),
            selectedSlot = selectedSlot,
            onSlotSelected = { selectedSlot = it },
            onConfirmAppointment = { /* Preview only */ }
        )
    }
}
