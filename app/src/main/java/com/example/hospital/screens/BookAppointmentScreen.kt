package com.example.hospital.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
    var selectedSlot by remember { mutableStateOf<String?>(null) }
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val id = user?.uid ?: "unknown_user"
    val namee = user?.displayName ?: "Anonymous"

    LaunchedEffect(doctorId) {
        db.collection("doctors").document(doctorId).get().addOnSuccessListener { doc ->
            doctor = doc.toObject(Doctor::class.java)?.copy(id = doc.id)
        }
    }

    BookAppointmentContent(
        doctor = doctor,
        selectedSlot = selectedSlot,
        onSlotSelected = { selectedSlot = it },
        onConfirmAppointment = {
            selectedSlot?.let { time ->
                val selectedTimestamp = convertSlotToTimestamp(time)  // ✅ Convert slot to Timestamp
                appointmentRepository.bookAppointment(
                    doctorId = doctorId,
                    doctorName = doctor?.name ?: "",
                    userName = namee,
                    appointmentTime = selectedTimestamp, // ✅ Use converted timestamp
                    onSuccess = { navController.popBackStack() },
                    onFailure = { e -> println("Error booking appointment: ${e.message}") }
                )
            }
        }
    )
}

@Composable
private fun BookAppointmentContent(
    doctor: Doctor?,
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

            LazyColumn {
                items(doc.availableSlots.size) { index ->
                    val slot = doc.availableSlots[index]

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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onConfirmAppointment,
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedSlot != null
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

fun convertSlotToTimestamp(slot: String): Timestamp {
    val slotParts = slot.split(" - ") // Split "9:00 AM - 10:00 AM"
    val timeFormat = SimpleDateFormat("h:mm a", Locale.US) // e.g., "9:00 AM"

    // Get today's date
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    // Parse the selected time slot (start time)
    val dateTimeString = "$currentDate ${slotParts[0]}" // "2025-02-16 9:00 AM"
    val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.US)
    val date = dateTimeFormat.parse(dateTimeString)!!

    return Timestamp(date) // ✅ Convert to Firebase Timestamp
}


@Preview(showBackground = true)
@Composable
fun BookAppointmentPreview() {
    // Mock data for preview
    val mockDoctor = Doctor(
        id = "preview_id",
        name = "Dr. John Doe",
        specialization = "Cardiologist",
        availableSlots = listOf(
            "9:00 AM - 10:00 AM",
            "10:00 AM - 11:00 AM",
            "11:00 AM - 12:00 PM",
            "2:00 PM - 3:00 PM",
            "3:00 PM - 4:00 PM"
        )
    )

    var selectedSlot by remember { mutableStateOf<String?>(null) }

    MaterialTheme {
        BookAppointmentContent(
            doctor = mockDoctor,
            selectedSlot = selectedSlot,
            onSlotSelected = { selectedSlot = it },
            onConfirmAppointment = { /* Preview only */ }
        )
    }
}