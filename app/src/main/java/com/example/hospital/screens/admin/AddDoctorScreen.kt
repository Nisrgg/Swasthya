package com.example.hospital.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hospital.data.models.Doctor
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AddDoctorScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var specialization by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedSlots by remember { mutableStateOf<List<String>>(emptyList()) }
    var availableSlots by remember { mutableStateOf<Map<String, List<String>>>(emptyMap()) }

    val db = FirebaseFirestore.getInstance()

    fun saveDoctor() {
        if (name.isNotEmpty() && specialization.isNotEmpty()) {
            val newDoctorRef = db.collection("doctors").document()
            val doctor = Doctor(
                id = newDoctorRef.id,
                name = name,
                specialization = specialization,
                availableSlots = availableSlots
            )

            newDoctorRef.set(doctor)
                .addOnSuccessListener { navController.popBackStack() }
                .addOnFailureListener { e -> println("Error adding doctor: ${e.message}") }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Add Doctor", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Doctor's Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = specialization,
            onValueChange = { specialization = it },
            label = { Text("Specialization") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calendar Date Picker
        com.example.hospital.screens.patient.DatePicker(onDateSelected = { selectedDate = it })

        Spacer(modifier = Modifier.height(16.dp))

        // Time Slot Selection
        TimeSlotSelector(selectedSlots, onSlotsChanged = { selectedSlots = it })

        Spacer(modifier = Modifier.height(16.dp))

        // Button to add slots for selected date
        Button(onClick = {
            if (selectedDate.isNotEmpty()) {
                availableSlots = availableSlots + (selectedDate to selectedSlots)
                selectedSlots = emptyList() // Reset slots after adding
            }
        }) {
            Text(text = "Add Slots for Date")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display added slots
        LazyColumn {
            items(availableSlots.entries.toList()) { entry ->
                Text("${entry.key}: ${entry.value.joinToString(", ")}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Doctor Button
        Button(onClick = { saveDoctor() }) {
            Text(text = "Save Doctor")
        }
    }
}

@Composable
fun TimeSlotSelector(selectedSlots: List<String>, onSlotsChanged: (List<String>) -> Unit) {
    val allSlots = listOf("9:00 AM", "10:00 AM", "11:00 AM", "2:00 PM", "3:00 PM")

    Column {
        allSlots.forEach { slot ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = slot, modifier = Modifier.padding(8.dp))
                Checkbox(
                    checked = selectedSlots.contains(slot),
                    onCheckedChange = { checked ->
                        val newSlots = if (checked) selectedSlots + slot else selectedSlots - slot
                        onSlotsChanged(newSlots)
                    }
                )
            }
        }
    }
}
