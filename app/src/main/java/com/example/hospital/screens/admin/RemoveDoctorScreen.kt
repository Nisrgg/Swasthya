package com.example.hospital.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.example.hospital.data.models.Doctor
import com.example.hospital.data.repositories.DoctorRepository
import kotlinx.coroutines.launch

@Composable
fun RemoveDoctorScreen() {
    val doctors = remember { mutableStateListOf<Doctor>() }
    val repository = DoctorRepository()
    val coroutineScope = rememberCoroutineScope()

    // Fetch doctors when the screen loads
    LaunchedEffect(Unit) {
        repository.getDoctors(
            onSuccess = { fetchedDoctors ->
                doctors.clear()
                doctors.addAll(fetchedDoctors)
            },
            onFailure = { error ->
                println("Error fetching doctors: ${error.message}")
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Remove Doctor",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (doctors.isEmpty()) {
                EmptyStateCard(message = "No doctors available", icon = "❌")
            } else {
                doctors.forEach { doctor ->
                    DoctorCard(doctor = doctor, onDelete = { doctorId ->
                        coroutineScope.launch {
                            repository.removeDoctor(
                                doctorId,
                                onSuccess = { doctors.removeIf { it.id == doctorId } },
                                onFailure = { e -> println("Error removing doctor: ${e.message}") }
                            )
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun DoctorCard(doctor: Doctor, onDelete: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = doctor.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = doctor.specialization,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(
                onClick = { onDelete(doctor.id) },
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Remove")
            }
        }
    }
}

@Composable
private fun EmptyStateCard(message: String, icon: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$icon  $message",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}