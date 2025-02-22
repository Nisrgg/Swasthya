package com.example.hospital.screens.admin

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hospital.data.models.Doctor
import com.example.hospital.data.repositories.DoctorRepository
import com.example.hospital.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveDoctorScreen() {
    var doctors by remember { mutableStateOf<List<Doctor>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val repository = DoctorRepository()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        repository.getDoctors(
            onSuccess = { fetchedDoctors ->
                doctors = fetchedDoctors
                isLoading = false
            },
            onFailure = { e ->
                error = e.message
                isLoading = false
            }
        )
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Remove Doctor") },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(
                visible = error != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                error?.let {
                    HospitalCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                doctors.isEmpty() -> {
                    EmptyStateCard()
                }
                else -> {
                    doctors.forEach { doctor ->
                        DoctorCard(
                            doctor = doctor,
                            onDelete = { doctorId ->
                                coroutineScope.launch {
                                    repository.removeDoctor(
                                        doctorId,
                                        onSuccess = {
                                            doctors = doctors.filter { it.id != doctorId }
                                        },
                                        onFailure = { e ->
                                            error = e.message
                                        }
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DoctorCard(doctor: Doctor, onDelete: (String) -> Unit) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    HospitalCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = doctor.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = doctor.specialization,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    doctor.availableSlots.takeIf { it.isNotEmpty() }?.let {
                        Text(
                            text = "${it.size} available time slots",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                TextButton(
                    onClick = { showConfirmDialog = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Remove")
                }
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Remove Doctor") },
            text = { Text("Are you sure you want to remove Dr. ${doctor.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(doctor.id)
                        showConfirmDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun EmptyStateCard() {
    HospitalCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Default.PersonOff,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "No doctors available",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RemoveDoctorScreenPreview() {
    HospitalTheme {
        RemoveDoctorScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorCardPreview() {
    HospitalTheme {
        DoctorCard(
            doctor = Doctor(
                id = "1",
                name = "Dr. John Doe",
                specialization = "Cardiologist",
                availableSlots = mapOf(
                    "2024-02-22" to listOf("9:00 AM", "10:00 AM")
                )
            ),
            onDelete = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyStateCardPreview() {
    HospitalTheme {
        EmptyStateCard()
    }
}