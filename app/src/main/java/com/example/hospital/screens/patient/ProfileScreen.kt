package com.example.hospital.screens.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hospital.data.models.UserProfile
import com.example.hospital.data.viewmodels.ProfileViewModel
import java.time.*
import java.time.format.DateTimeFormatter
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateBack: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var isEditMode by remember { mutableStateOf(false) }

    // Form states
    var fullName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var emergencyContact by remember { mutableStateOf("") }
    var medicalConditions by remember { mutableStateOf("") }

    val colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    // Update local state when profile loaded
    LaunchedEffect(profile) {
        profile?.let {
            fullName = it.fullName
            dateOfBirth = it.dateOfBirth
            gender = it.gender
            phoneNumber = it.phoneNumber
            email = it.email
            address = it.address
            bloodGroup = it.bloodGroup
            emergencyContact = it.emergencyContact
            medicalConditions = it.medicalConditions
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { isEditMode = !isEditMode }) {
                        Icon(
                            if (isEditMode) Icons.Default.Close else Icons.Default.Edit,
                            contentDescription = if (isEditMode) "Cancel" else "Edit"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        enabled = isEditMode,
                        modifier = Modifier.fillMaxWidth(),
                        colors = colors

                    )
                }

                item {
                    OutlinedTextField(
                        value = dateOfBirth,
                        onValueChange = {},
                        label = { Text("Date of Birth") },
                        enabled = isEditMode,
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            if (isEditMode) {
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(Icons.Default.DateRange, "Select Date")
                                }
                            }
                        },
                        colors = colors
                    )
                }

                // Gender Dropdown
                item {
                    val genders = listOf("Male", "Female", "Other")
                    var expanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { if (isEditMode) expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = {},
                            label = { Text("Gender") },
                            readOnly = true,
                            enabled = isEditMode,
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                if (isEditMode) {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                }
                            },
                            colors = colors
                        )

                        if (isEditMode) {
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                genders.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            gender = option
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Other fields
                item {
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Phone Number") },
                        enabled = isEditMode,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        colors = colors
                    )
                }

                item {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        enabled = isEditMode,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        colors = colors
                    )
                }

                item {
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Address") },
                        enabled = isEditMode,
                        modifier = Modifier.fillMaxWidth(),
                        colors = colors
                    )
                }

                // Blood Group Dropdown
                item {
                    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
                    var expanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { if (isEditMode) expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = bloodGroup,
                            onValueChange = {},
                            label = { Text("Blood Group") },
                            readOnly = true,
                            enabled = isEditMode,
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                if (isEditMode) {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                }
                            },
                            colors = colors
                        )

                        if (isEditMode) {
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                bloodGroups.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            bloodGroup = option
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = emergencyContact,
                        onValueChange = { emergencyContact = it },
                        label = { Text("Emergency Contact") },
                        enabled = isEditMode,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        colors = colors
                    )
                }

                item {
                    OutlinedTextField(
                        value = medicalConditions,
                        onValueChange = { medicalConditions = it },
                        label = { Text("Medical Conditions") },
                        enabled = isEditMode,
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        colors = colors
                    )
                }

                if (isEditMode) {
                    item {
                        Button(
                            onClick = {
                                val updatedProfile = UserProfile(
                                    userId = profile?.userId ?: "",
                                    fullName = fullName,
                                    dateOfBirth = dateOfBirth,
                                    gender = gender,
                                    phoneNumber = phoneNumber,
                                    email = email,
                                    address = address,
                                    bloodGroup = bloodGroup,
                                    emergencyContact = emergencyContact,
                                    medicalConditions = medicalConditions
                                )
                                viewModel.saveProfile(updatedProfile)
                                isEditMode = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val localDateTime = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(millis),
                            ZoneId.systemDefault()
                        )
                        dateOfBirth =
                            localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun ProfileScreenPreview() {
//    // Create a mock ProfileViewModel for the preview
//    val mockViewModel = ProfileViewModel().apply {
//        // Set up a mock profile
//        profile.value = UserProfile(
//            userId = "1",
//            fullName = "John Doe",
//            dateOfBirth = "01/01/1990",
//            gender = "Male",
//            phoneNumber = "1234567890",
//            email = "john.doe@example.com",
//            address = "123 Main St",
//            bloodGroup = "O+",
//            emergencyContact = "0987654321",
//            medicalConditions = "None"
//        )
//    }
//
//    ProfileScreen(viewModel = mockViewModel, onNavigateBack = {})
//}