package com.example.hospital.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospital.data.models.Doctor
import com.example.hospital.ui.theme.*
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDoctorScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var specialization by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedSlots by remember { mutableStateOf<List<String>>(emptyList()) }
    var availableSlots by remember { mutableStateOf<Map<String, List<String>>>(emptyMap()) }
    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val db = FirebaseFirestore.getInstance()

    fun saveDoctor() {
        if (name.isNotEmpty() && specialization.isNotEmpty()) {
            isLoading = true
            showError = false
            val newDoctorRef = db.collection("doctors").document()
            val doctor = Doctor(
                id = newDoctorRef.id,
                name = name,
                specialization = specialization,
                availableSlots = availableSlots
            )

            newDoctorRef.set(doctor)
                .addOnSuccessListener {
                    isLoading = false
                    navController.popBackStack()
                }
                .addOnFailureListener { e ->
                    isLoading = false
                    showError = true
                }
        } else {
            showError = true
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Add New Doctor") },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                if (showError) {
                    HospitalCard {
                        Text(
                            text = "Please fill in all required fields",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            item {
                HospitalTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Doctor's Name"
                )
            }

            item {
                HospitalTextField(
                    value = specialization,
                    onValueChange = { specialization = it },
                    label = "Specialization"
                )
            }

            item {
                HospitalCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SectionTitle(title = "Select Available Date")
                        com.example.hospital.screens.patient.DatePicker(onDateSelected = { selectedDate = it })
                    }
                }
            }

            item {
                HospitalCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SectionTitle(title = "Select Time Slots")
                        TimeSlotSelector(selectedSlots, onSlotsChanged = { selectedSlots = it })
                    }
                }
            }

            item {
                PrimaryButton(
                    text = "Add Slots for Selected Date",
                    onClick = {
                        if (selectedDate.isNotEmpty()) {
                            availableSlots = availableSlots + (selectedDate to selectedSlots)
                            selectedSlots = emptyList()
                        }
                    },
                    enabled = selectedDate.isNotEmpty() && selectedSlots.isNotEmpty()
                )
            }

            items(availableSlots.entries.toList()) { entry ->
                HospitalCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Date: ${entry.key}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Slots: ${entry.value.joinToString(", ")}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                PrimaryButton(
                    text = if (isLoading) "Saving..." else "Save Doctor",
                    onClick = { saveDoctor() },
                    enabled = !isLoading
                )
            }
        }
    }
}

@Composable
fun TimeSlotSelector(selectedSlots: List<String>, onSlotsChanged: (List<String>) -> Unit) {
    val allSlots = listOf("9:00 AM", "10:00 AM", "11:00 AM", "2:00 PM", "3:00 PM")

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        allSlots.forEach { slot ->
            HospitalCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = slot,
                        style = MaterialTheme.typography.bodyLarge
                    )
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
}

@Preview(showBackground = true)
@Composable
fun AddDoctorScreenPreview() {
    HospitalTheme {
        AddDoctorScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun TimeSlotSelectorPreview() {
    HospitalTheme {
        TimeSlotSelector(
            selectedSlots = listOf("9:00 AM", "2:00 PM"),
            onSlotsChanged = {}
        )
    }
}